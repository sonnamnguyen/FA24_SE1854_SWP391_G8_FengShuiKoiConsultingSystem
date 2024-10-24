import React, { useEffect, useState } from "react";
import { Form, Input, Button, DatePicker, Upload, notification } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { getToken } from "../service/localStorageService";
import { useNavigate } from "react-router-dom";
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../firebase/firebase"; // Ensure this is the correct path
import { jwtDecode } from "jwt-decode";
import api from "../axious/axious";

const UpdateProfile: React.FC = () => {
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<File[]>([]);
  const [email, setEmail] = useState("");
  const [apii, contextHolder] = notification.useNotification();
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    if (token) {
      const userData: any = jwtDecode(token); // Decode the token to get user info
      if (userData) {
        setEmail(userData.sub || "");
      }
    }
  }, []);

  const handleUploadChange = (info: any) => {
    const newFileList = info.fileList.slice(-1); // Keep only the last file
    setFileList(newFileList.map((file: any) => file.originFileObj));
  };

  // Convert file to base64
  const getBase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
    });
  };

  // Upload image to Firebase
  const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
    const uploadPromises = files.map(async (file) => {
      const storageRef = ref(storage, `avatars/${file.name}`);
      const base64Image = await getBase64(file);
      await uploadString(storageRef, base64Image, 'data_url');
      const downloadURL = await getDownloadURL(storageRef);
      return downloadURL;
    });
    return Promise.all(uploadPromises);
  };

  const handleSubmit = async (values: any) => {
    const { userName, password, fullName, dob, phoneNumber, gender, email } = values; // Destructured `email`
  
    try {
      const avatar = fileList.length > 0 ? await uploadImagesToFirebase(fileList) : null;
  
      const formattedDob = dob instanceof Date ? dob.toISOString().split('T')[0] : dob;
  
      const response = await api.put(`/users/${email}`, {
        userName,
        password,
        email,
        dob: formattedDob,
        phoneNumber,
        gender,
        avatar: avatar ? avatar[0] : null,
      });
  
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
        navigate("/profile");
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
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
        className="col-md-6 col-12 mx-auto"
        initialValues={{ email }} // Pre-fill email if available
      >
        <Form.Item
          label="Username"
          name="userName"
          rules={[{ required: true, message: "Please input your username!" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Password"
          name="password"
          rules={[{ required: true, message: "Please input your password!" }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          label="Confirm Password"
          name="confirmPassword"
          dependencies={["password"]}
          rules={[
            { required: true, message: "Please confirm your password!" },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("password") === value) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error("The two passwords do not match!"));
              },
            }),
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          label="Full Name"
          name="fullName"
          rules={[{ required: true, message: "Please input your full name!" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Date of Birth"
          name="dob"
          rules={[{ required: true, message: "Please select your date of birth!" }]}
        >
          <DatePicker style={{ width: "100%" }} />
        </Form.Item>

        <Form.Item
          label="Phone Number"
          name="phoneNumber"
          rules={[
            { required: true, message: "Please input your phone number!" },
            { pattern: /^\d{10}$/, message: "Phone number must be 10 digits" },
          ]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Avatar"
        >
          <Upload
            maxCount={1} // Only allow 1 image
            accept="image/*"
            showUploadList={true}
            beforeUpload={() => false} // Prevent immediate upload
            onChange={handleUploadChange}
            listType="picture-card"
          >
            <div>
              <PlusOutlined />
              <div style={{ marginTop: 8 }}>Upload</div>
            </div>
          </Upload>
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default UpdateProfile;
