import React, { useEffect, useState } from "react";
import { Input, Button, DatePicker, Upload, notification } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { getToken } from "../service/localStorageService";
import { useNavigate } from "react-router-dom";
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../firebase/firebase";
import api from "../axious/axious";
import { jwtDecode } from "jwt-decode";

const UpdateProfile: React.FC = () => {
  const [fileList, setFileList] = useState<File[]>([]);
  const [email, setEmail] = useState("");
  const [apii, contextHolder] = notification.useNotification();
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    if (token) {
      const userData: any = jwtDecode(token);
      if (userData) {
        setEmail(userData.sub || "");
      }
    }
  }, []);

  const handleUploadChange = (info: any) => {
    const newFileList = info.fileList.slice(-1);
    setFileList(newFileList.map((file: any) => file.originFileObj));
  };

  const getBase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
    });
  };

  const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
    const uploadPromises = files.map(async (file) => {
      const storageRef = ref(storage, `avatars/${file.name}`);
      const base64Image = await getBase64(file);
      await uploadString(storageRef, base64Image, "data_url");
      return getDownloadURL(storageRef);
    });
    return Promise.all(uploadPromises);
  };

  const validationSchema = Yup.object({
    userName: Yup.string()
      .min(3, "Username must be between 3 and 50 characters")
      .max(50, "Username must be between 3 and 50 characters")
      .required("Username is required"),
    fullName: Yup.string()
      .min(3, "Full name must be between 3 and 100 characters")
      .max(100, "Full name must be between 3 and 100 characters")
      .required("Full name is required"),
    dob: Yup.date().required("Date of birth is required"),
    phoneNumber: Yup.string()
      .matches(/^\d{10}$/, "Phone number must be 10 digits")
      .required("Phone number is required"),
  });

  const handleSubmit = async (values: any) => {
    const { userName, password, fullName, dob, phoneNumber, gender } = values;
    try {
      const avatar =
        fileList.length > 0 ? await uploadImagesToFirebase(fileList) : null;
      const data = {
        userName,
        email,
        dob: dob ? dob.format("YYYY-MM-DD") : "",
        phoneNumber,
        avatar: avatar ? avatar[0] : null,
      };
      const response = await api.put(`/users/${email}`, data);
      if (response.data.code !== 1000) {
        apii.error({
          message: "Error",
          description: "Failed to update profile.",
        });
      } else {
        apii.success({
          message: "Success",
          description: "Profile has been successfully updated.",
        });
      }
    } catch (error) {
      apii.error({
        message: "Error",
        description: "Error updating profile.",
      });
    }
  };

  return (
    <div className="container">
      {contextHolder}
      <h1 className="mt-5 text-center">Update Profile</h1>
      <Formik
        initialValues={{
          userName: "",
          fullName: "",
          dob: null,
          phoneNumber: "",
        }}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        {({ setFieldValue, values }) => (
          <Form className="col-md-6 col-12 mx-auto">
            <div className="form-item">
              <label>Username</label>
              <Field name="userName" type="text" className="form-control" />
              <ErrorMessage
                name="userName"
                component="div"
                className="error-message"
              />
            </div>

            <div className="form-item">
              <label>Full Name</label>
              <Field name="fullName" type="text" className="form-control" />
              <ErrorMessage
                name="fullName"
                component="div"
                className="error-message"
              />
            </div>

            <div className="form-item">
              <label>Date of Birth</label>
              <DatePicker
                onChange={(date) => setFieldValue("dob", date)}
                value={values.dob}
                style={{ width: "100%" }}
              />
              <ErrorMessage
                name="dob"
                component="div"
                className="error-message"
              />
            </div>

            <div className="form-item">
              <label>Phone Number</label>
              <Field name="phoneNumber" type="text" className="form-control" />
              <ErrorMessage
                name="phoneNumber"
                component="div"
                className="error-message"
              />
            </div>

            <div className="form-item">
              <label>Avatar</label>
              <Upload
                maxCount={1}
                accept="image/*"
                showUploadList={true}
                beforeUpload={() => false}
                onChange={handleUploadChange}
                listType="picture-card"
              >
                <div>
                  <PlusOutlined />
                  <div style={{ marginTop: 8 }}>Upload</div>
                </div>
              </Upload>
            </div>

            <Button type="primary" htmlType="submit">
              Submit
            </Button>
          </Form>
        )}
      </Formik>
    </div>
  );
};

export default UpdateProfile;
