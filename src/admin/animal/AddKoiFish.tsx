import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Modal, Button } from 'react-bootstrap';
import { Checkbox, Divider, Upload, Form as AntdForm, Input } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../../firebase/firebase";
import api from "../../axious/axious";
import { useFormik } from 'formik';
import * as Yup from 'yup';

interface Colors {
  id: number;
  color: string;
}

const CheckboxGroup = Checkbox.Group;

const AddKoiFish: React.FC = () => {
  const [animalImages, setAnimalImages] = useState<File[]>([]);
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

  const onChange = (list: number[]) => setCheckedList(list);
  const onCheckAllChange = (e: any) => setCheckedList(e.target.checked ? plainOptions.map(option => option.id) : []);

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

  const formik = useFormik({
    initialValues: {
      animalCategoryName: "",
      description: "",
      origin: ""
    },
    validationSchema: Yup.object({
      animalCategoryName: Yup.string()
          .required("Animal category name is required")
          .min(3, "Must be at least 3 characters")
          .max(50, "Must be 50 characters or less"),
      description: Yup.string()
          .max(200, "Description can be up to 200 characters"),
      origin: Yup.string()
          .required("Origin is required")
          .min(2, "Must be at least 2 characters")
          .max(30, "Must be 30 characters or less")
    }),
    onSubmit: async (values) => {
      const base64Avatars = animalImages.length > 0 ? await uploadImagesToFirebase(animalImages) : [];

      try {
        const response = await api.post("/animals", {
          ...values,
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
    }
  });

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
        <form onSubmit={formik.handleSubmit}>
          <AntdForm.Item label="Name">
            <Input
                {...formik.getFieldProps('animalCategoryName')}
            />
            {formik.touched.animalCategoryName && formik.errors.animalCategoryName ? (
                <div className="error-message">{formik.errors.animalCategoryName}</div>
            ) : null}
          </AntdForm.Item>

          <AntdForm.Item label="Description">
            <Input
                {...formik.getFieldProps('description')}
            />
            {formik.touched.description && formik.errors.description ? (
                <div className="error-message">{formik.errors.description}</div>
            ) : null}
          </AntdForm.Item>

          <AntdForm.Item label="Origin">
            <Input
                {...formik.getFieldProps('origin')}
            />
            {formik.touched.origin && formik.errors.origin ? (
                <div className="error-message">{formik.errors.origin}</div>
            ) : null}
          </AntdForm.Item>

          <AntdForm.Item label="Avatars">
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
          </AntdForm.Item>

          <AntdForm.Item>
            <Checkbox indeterminate={indeterminate} onChange={onCheckAllChange} checked={checkAll}>
              Check all
            </Checkbox>
            <Divider />
            <CheckboxGroup
                options={plainOptions.map(option => ({ label: option.color, value: option.id }))}
                value={checkedList}
                onChange={onChange}
            />
          </AntdForm.Item>

          <button type="submit" className="btn-primary">Submit</button>
        </form>

        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title style={{ color: modalNotificationColor }}>{modalNotification}</Modal.Title>
          </Modal.Header>
          <Modal.Body>{modalMessage}</Modal.Body>
          <Modal.Footer>
            {modalNotification === "Success" ? (
                <Button variant="primary" onClick={handleGoToLogin}>Go to Login</Button>
            ) : (
                <Button variant="secondary" onClick={handleCloseModal}>Close</Button>
            )}
          </Modal.Footer>
        </Modal>
      </div>
  );
};

export default AddKoiFish;
