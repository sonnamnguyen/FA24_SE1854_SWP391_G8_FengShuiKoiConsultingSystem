import React, { useState } from "react";
import { Form, Input, Button, DatePicker, Upload, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { ref, uploadBytes, getDownloadURL } from "firebase/storage";
import { storage } from "../../src/firebase/firebase"; // Adjust the path based on your structure
import type { UploadChangeParam, UploadFile } from "antd/es/upload/interface";
import api from "../axious/axious";

function Register() {
  const [form] = Form.useForm();
  const [avatar, setAvatar] = useState<File | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalNotification, setModalNotification] = useState("");
  const [modalNotificationColor, setModalNotificationColor] = useState("red");
  const navigate = useNavigate();

  const handleAvatarChange = ({ file }: UploadChangeParam<UploadFile<any>>) => {
    // Save the file to the state for later upload
    if (file && file.originFileObj) {
      setAvatar(file.originFileObj); // Ensure you are storing the correct file object
    }
  };

  const handleSubmit = async (values: any) => {
    const { userName, password, fullName, email, dob, phoneNumber, gender } = values;

    try {
      let avatarURL = "";
      if (avatar) {
        const storageRef = ref(storage, `avatars/${avatar.name}`);
        const snapshot = await uploadBytes(storageRef, avatar);
        avatarURL = await getDownloadURL(snapshot.ref); 
      }

      const response = await api.post("/users", {
        userName,
        password,
        fullName,
        email,
        dob: dob.format("YYYY-MM-DD"), // Ensure dob is formatted correctly
        phoneNumber,
        gender,
        avatar: avatarURL, // Save the avatar URL
      });

     
      if (response.data.code !== 1000) {
        throw new Error(response.data.message);
      } else {
        setModalMessage("Profile registered successfully");
        setModalNotification("Success");
        setModalNotificationColor("green");
        setShowModal(true);
      }
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

        <Form.Item label="Avatar" name="avatar">
          <Upload
            accept="image/*"
            showUploadList={false}
            beforeUpload={() => false} // Prevent automatic upload
            onChange={handleAvatarChange}
            listType="picture-card"

          >
            <Button icon={<PlusOutlined />}>Upload Avatar</Button>
          </Upload>
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" className="btn btn-primary">
            Submit
          </Button>
        </Form.Item>
      </Form>

      <Modal
        visible={showModal}
        onCancel={() => setShowModal(false)}
        footer={
          <Button type="primary" onClick={handleGoToLogin}>
            Go to Login
          </Button>
        }
        title={modalNotification}
        bodyStyle={{ color: modalNotificationColor }}
      >
        {modalMessage}
      </Modal>
    </div>
  );
}

export default Register;
