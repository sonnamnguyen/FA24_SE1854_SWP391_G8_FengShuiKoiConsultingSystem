import React, { useState } from "react";
import { Form, Input, Button, DatePicker, Upload, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { useNavigate } from "react-router-dom";
import api from "../axious/axious";
import { storage } from "../firebase/firebase";

const Register: React.FC = () => {
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<File[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalNotification, setModalNotification] = useState("");
  const [modalNotificationColor, setModalNotificationColor] = useState("red");
  const navigate = useNavigate();

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
    const { userName, password, fullName, email, dob, phoneNumber, gender } = values;
  
    try {
      // Upload avatar image to Firebase if the fileList is not empty
      const avatar = fileList.length > 0 ? await uploadImagesToFirebase(fileList) : null;
  
      // Prepare data to be sent in the POST request
      const data = {
        userName,
        password,
        fullName,
        email,
        dob: dob.format("YYYY-MM-DD"), // Format the date of birth
        phoneNumber,
        gender,
        avatar: avatar ? avatar[0] : null, // Use the first uploaded image as avatar
      };
  
      // Send the POST request to create a new user
      const response = await fetch(`http://localhost:9090/users`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
  
      // Parse the JSON response
      const result = await response.json();
  
      // Check if the request was successful
      if (result.code !== 1000) {
        throw new Error(result.message); // Throw an error if the code is not 1000
      }
  
      // Show success message
      setModalMessage("Profile registered successfully. Please check your email for the authentication code.");
      setModalNotification("Success");
      setModalNotificationColor("green");
      setShowModal(true);
    } catch (error) {
      // Show error message in case of failure
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
    <div className="container">
      <h1 className="mt-5 text-center">Register</h1>
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
        className="col-md-6 col-12 mx-auto"
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
          label="Email"
          name="email"
          rules={[
            { type: "email", message: "The input is not valid E-mail!" },
            { required: true, message: "Please input your E-mail!" },
          ]}
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
          label="Gender"
          name="gender"
          rules={[{ required: true, message: "Please input your gender!" }]}
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

      <Modal
        open={showModal}
        onCancel={() => setShowModal(false)}
        footer={
          <Button type="primary" onClick={handleGoToLogin}>
            Go to Login
          </Button>
        }
        title={modalNotification}
        style={{ color: modalNotificationColor }}
      >
        {modalMessage}
      </Modal>
    </div>
  );
};

export default Register;
