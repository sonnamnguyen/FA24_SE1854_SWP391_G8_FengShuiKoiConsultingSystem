import React, { useState } from "react";
import { Button, DatePicker, Upload, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { useNavigate } from "react-router-dom";
import { storage } from "../firebase/firebase";
import "../css/Register.css";

const Register: React.FC = () => {
  const [fileList, setFileList] = useState<File[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalNotification, setModalNotification] = useState("");
  const [modalNotificationColor, setModalNotificationColor] = useState("red");
  const navigate = useNavigate();

  const handleUploadChange = (info: any) => {
    const newFileList = info.fileList.slice(-1); // Chỉ giữ lại file cuối cùng
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
      const downloadURL = await getDownloadURL(storageRef);
      return downloadURL;
    });
    return Promise.all(uploadPromises);
  };

  const validationSchema = Yup.object({
    userName: Yup.string()
      .min(3, "Username must be between 3 and 50 characters")
      .max(50, "Username must be between 3 and 50 characters")
      .required("Username is required"),
    password: Yup.string()
      .min(8, "Password must be between 8 and 15 characters")
      .max(15, "Password must be between 8 and 15 characters")
      .matches(
        /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/,
        "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
      )
      .required("Password is required"),
    confirmPassword: Yup.string()
      .oneOf([Yup.ref("password")], "Passwords must match")
      .required("Confirm your password"),
    fullName: Yup.string()
      .min(3, "Full name must be between 3 and 100 characters")
      .max(100, "Full name must be between 3 and 100 characters")
      .required("Full name is required"),
    email: Yup.string()
      .email("Invalid email format")
      .required("Email is required"),
    dob: Yup.date().required("Date of birth is required"),
    phoneNumber: Yup.string()
      .matches(/^\d{10}$/, "Phone number must be 10 digits")
      .required("Phone number is required"),
    gender: Yup.string()
      .oneOf(
        ["MALE", "FEMALE", "OTHER"],
        "Gender must be MALE, FEMALE, or OTHER"
      )
      .required("Gender is required"),
  });

  const handleSubmit = async (values: any) => {
    try {
      const avatar =
        fileList.length > 0 ? await uploadImagesToFirebase(fileList) : null;
      const data = {
        ...values,
        dob: values.dob.format("YYYY-MM-DD"),
        avatar: avatar ? avatar[0] : null,
      };
      const response = await fetch("http://localhost:9090/users", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
      const result = await response.json();
      if (result.code !== 1000) {
        throw new Error(result.message);
      }
      setModalMessage("Profile registered successfully.");
      setModalNotification("Success");
      setModalNotificationColor("green");
      setShowModal(true);
    } catch (error) {
      setModalMessage("An error occurred while registering your profile");
      setModalNotification("Failed");
      setModalNotificationColor("red");
      setShowModal(true);
    }
  };

  const handleGoToLogin = () => {
    setShowModal(false);
    navigate("/login");
  };

  return (
    <div className="Register-container">
      <Formik
        initialValues={{
          userName: "",
          password: "",
          confirmPassword: "",
          fullName: "",
          email: "",
          dob: null,
          phoneNumber: "",
          gender: "",
        }}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        {({ setFieldValue, values }) => (
          <Form className="col-md-6 col-12 mx-auto Register-form">
            <h1 className="mt-5 text-center Register-text">Sign Up</h1>
            <div className="info">
              <div className="left-info">
                <div className="form-item Register-form-item">
                  <label>Username</label>
                  <Field name="userName" type="text" className="form-control" />
                  <ErrorMessage
                    name="userName"
                    component="div"
                    className="error-message"
                  />
                </div>

                <div className="form-item Register-form-item">
                  <label>Password</label>
                  <Field
                    name="password"
                    type="password"
                    className="form-control"
                  />
                  <ErrorMessage
                    name="password"
                    component="div"
                    className="error-message"
                  />
                </div>

                <div className="form-item Register-form-item">
                  <label>Confirm Password</label>
                  <Field
                    name="confirmPassword"
                    type="password"
                    className="form-control"
                  />
                  <ErrorMessage
                    name="confirmPassword"
                    component="div"
                    className="error-message"
                  />
                </div>

                <div className="form-item Register-form-item">
                  <label>Full Name</label>
                  <Field name="fullName" type="text" className="form-control" />
                  <ErrorMessage
                    name="fullName"
                    component="div"
                    className="error-message"
                  />
                </div>
                <div className="form-item" Register-form-item>
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
              </div>
              <div className="right-info">
                <div className="form-item" Register-form-item>
                  <label>Email</label>
                  <Field name="email" type="email" className="form-control" />
                  <ErrorMessage
                    name="email"
                    component="div"
                    className="error-message"
                  />
                </div>

                <div className="form-item" Register-form-item>
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

                <div className="form-item" Register-form-item>
                  <label>Phone Number</label>
                  <Field
                    name="phoneNumber"
                    type="text"
                    className="form-control"
                  />
                  <ErrorMessage
                    name="phoneNumber"
                    component="div"
                    className="error-message"
                  />
                </div>

                <div className="form-item" Register-form-item>
                  <label>Gender</label>
                  <Field name="gender" as="select" className="form-control">
                    <option value="MALE">MALE</option>
                    <option value="FEMALE">FEMALE</option>
                    <option value="OTHER">OTHER</option>
                  </Field>
                  <ErrorMessage
                    name="gender"
                    component="div"
                    className="error-message"
                  />
                </div>
                <div>
                  <Button className="register-btn" type="primary" htmlType="submit">
                    Submit
                  </Button>
                  <div>
                    <span> You have an account? </span>
                    <a
                      onClick={() => navigate("/login")}
                      className="login-page-signup-link-text"
                    >
                      Sign in now
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </Form>
        )}
      </Formik>

      <Modal
        open={showModal}
        onCancel={() => setShowModal(false)}
        footer={<Button onClick={handleGoToLogin}>Go to Login</Button>}
        title={modalNotification}
        style={{ color: modalNotificationColor }}
      >
        {modalMessage}
      </Modal>
    </div>
  );
};

export default Register;