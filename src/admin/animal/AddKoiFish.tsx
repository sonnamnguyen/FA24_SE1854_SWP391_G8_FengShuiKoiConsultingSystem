import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Modal, Button } from 'react-bootstrap';
import { Checkbox, Divider, Upload, Form as AntdForm, Input, notification } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../../firebase/firebase";
import { useFormik } from 'formik';
import * as Yup from 'yup';
import api from "../../axious/axious";

interface Colors {
  id: number;
  color: string;
}

const CheckboxGroup = Checkbox.Group;

const AddKoiFish: React.FC = () => {
  const [animalImages, setAnimalImages] = useState<File[]>([]);
  const [apiNotification, contextHolder] = notification.useNotification();
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
      console.error("Failed to fetch colors: ", response.data.message);
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

        const data = await response.data;
        
        if (data.code === 1000) {
          apiNotification.success({ message: 'Success', description: 'Koi Fish has been successfully added.' });
          formik.resetForm();
          setAnimalImages([]);
          setCheckedList([]);
        } else {
          apiNotification.error({ message: 'Error', description: data.message });
        }
      } catch (error: any) {
        console.error("Error occurred during submission:", error);
        apiNotification.error({
          message: 'Error',
          description: error.response?.data?.message || 'An unexpected error occurred while adding Koi Fish.'
        });
      }
    }
  });

  const handleUploadChange = (info: any) => {
    const { fileList } = info;
    setAnimalImages(fileList.map((file: any) => file.originFileObj));
  };

  return (
    <div className="container">
      {contextHolder}
      <h1 className="mt-5">Koi Fish</h1>
      <form onSubmit={formik.handleSubmit}>
        <AntdForm.Item label="Name">
          <Input
            {...formik.getFieldProps('animalCategoryName')}
          />
          {formik.touched.animalCategoryName && formik.errors.animalCategoryName && (
            <div className="error-message">{formik.errors.animalCategoryName}</div>
          )}
        </AntdForm.Item>

        <AntdForm.Item label="Description">
          <Input
            {...formik.getFieldProps('description')}
          />
          {formik.touched.description && formik.errors.description && (
            <div className="error-message">{formik.errors.description}</div>
          )}
        </AntdForm.Item>

        <AntdForm.Item label="Origin">
          <Input
            {...formik.getFieldProps('origin')}
          />
          {formik.touched.origin && formik.errors.origin && (
            <div className="error-message">{formik.errors.origin}</div>
          )}
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
    </div>
  );
};

export default AddKoiFish;
