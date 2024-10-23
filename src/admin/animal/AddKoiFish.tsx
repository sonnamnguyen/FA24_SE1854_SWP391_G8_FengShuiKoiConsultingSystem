import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Modal, Button } from 'react-bootstrap';
import { getToken, setToken } from "../../service/localStorageService";
import { Checkbox, Divider, Upload, Form, Input } from 'antd';
import type { CheckboxProps } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../../firebase/firebase";
import api from "../../axious/axious";

interface Colors {
  id: number;
  color: string;
}

const CheckboxGroup = Checkbox.Group;

const AddKoiFish: React.FC = () => {
  const [animalCategoryName, setAnimalCategoryName] = useState("");
  const [description, setDescription] = useState("");
  const [origin, setOrigin] = useState("");
  const [animalImages, setAnimalImages] = useState<File[]>([]);
  const [errorMessages, setErrorMessages] = useState({ name: "", description: "", origin: "" });
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalNotification, setModalNotification] = useState("");
  const [modalNotificationColor, setModalNotificationColor] = useState("red");
  const [plainOptions, setPlainOptions] = useState<Colors[]>([]);
  const [checkedList, setCheckedList] = useState<number[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchColors = async () => {
      const colors = await getAllColors();
      if (colors) {
        setPlainOptions(colors);
      }
    };
    fetchColors();
  }, []);

  const checkAll = plainOptions.length === checkedList.length;
  const indeterminate = checkedList.length > 0 && checkedList.length < plainOptions.length;

  const onChange = (list: number[]) => {
    setCheckedList(list);
  };

  const onCheckAllChange: CheckboxProps['onChange'] = (e) => {
    setCheckedList(e.target.checked ? plainOptions.map(option => option.id) : []);
  };

  const getAllColors = async (): Promise<Colors[] | null> => {
    try {
      const response = await api.get('/colors/getAll-Colors');
      if (response.data.code === 1000) {
        return response.data.result.map((color: any) => ({ id: color.id, color: color.color }));
      }
      console.error("Failed to fetch colors: ", response.status);
      return null;
    } catch (error) {
      console.error("Error fetching colors: ", error);
      return null;
    }
  };

  const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
    const uploadPromises = files.map(async (file) => {
      const storageRef = ref(storage, `koi_images/${file.name}`);
      const base64Image = await getBase64(file);
      await uploadString(storageRef, base64Image, 'data_url');
      const downloadURL = await getDownloadURL(storageRef);
      return downloadURL;
    });

    return Promise.all(uploadPromises);
  };

  const getBase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
    });
  };

  const handleSubmit = async () => {
    setErrorMessages({ name: "", description: "", origin: "" });


    const base64Avatars = animalImages.length > 0 ? await uploadImagesToFirebase(animalImages) : [];

    try {
      const response = await api.post("/animals", {
        animalCategoryName,
        description,
        origin,
        animalImages: base64Avatars.map(url => ({ imageUrl: url })),
        colors: checkedList.map(id => ({ id }))
      });

      if (response.data.code !== 1000) {
        throw new Error(response.data.message);
      } else {
        setModalMessage("Animal created successfully");
        setModalNotification("Success");
        setModalNotificationColor("green");
      }
    } catch (error) {
      setModalMessage("An error occurred while creating the animal");
      setModalNotification("Failed");
      setModalNotificationColor("red");
    } finally {
      setShowModal(true);
    }
  };



  const handleUploadChange = (info: any) => {
    const { fileList } = info;
    setAnimalImages(fileList.map((file: any) => file.originFileObj));
  };

  const handleCloseModal = () => setShowModal(false);
  const handleGoToLogin = () => {
    setShowModal(false);
    navigate("/login");
  };

  return (
    <div className="container">
      <h1 className="mt-5">Koi Fish</h1>
      <Form onFinish={handleSubmit}>
        <Form.Item
          label="Name"
          rules={[{ required: true, message: "Please input the name of the Koi fish" }]}
        >
          <Input
            value={animalCategoryName}
            onChange={(e) => setAnimalCategoryName(e.target.value)}
          />
          {errorMessages.name && <div className="error-message">{errorMessages.name}</div>}
        </Form.Item>

        <Form.Item
          label="Description"
          rules={[{ required: true, message: "Please input a description for the Koi fish" }]}
        >
          <Input
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
          {errorMessages.description && <div className="error-message">{errorMessages.description}</div>}
        </Form.Item>

        <Form.Item
          label="Origin"
          rules={[{ required: true, message: "Please input the origin of the Koi fish" }]}
        >
          <Input
            value={origin}
            onChange={(e) => setOrigin(e.target.value)}
          />
          {errorMessages.origin && <div className="error-message">{errorMessages.origin}</div>}
        </Form.Item>

        <Form.Item label="Avatars" className="form-item">
          <Upload
            multiple
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
        </Form.Item>

        <Form.Item className="form-item">
          <Checkbox indeterminate={indeterminate} onChange={onCheckAllChange} checked={checkAll}>
            Check all
          </Checkbox>
          <Divider />
          <CheckboxGroup
            options={plainOptions.map(option => ({ label: option.color, value: option.id }))}
            value={checkedList}
            onChange={onChange}
          />
        </Form.Item>

        <Form.Item className="form-item">
          <button type="submit" className="btn-primary">Submit</button>
        </Form.Item>
      </Form>

      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title style={{ color: modalNotificationColor }}>{modalNotification}</Modal.Title>
        </Modal.Header>
        <Modal.Body>{modalMessage}</Modal.Body>
        <Modal.Footer>
          {modalNotification === "Success" ? (
            <Button variant="primary" onClick={handleGoToLogin}>
              Go to Login
            </Button>
          ) : (
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
          )}
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default AddKoiFish;
