import React, { useEffect, useState } from "react";
import { notification, Upload, Form, Input, Radio } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../../firebase/firebase";
import * as Yup from 'yup'; // Import Yup for validation
import { Formik } from 'formik'; // Import Formik for form handling
import api from "../../axious/axious";
import '../../css/addKoi.css';

interface Shapes {
  id: number;
  shape: string;
}

const AddShelter: React.FC = () => {
  const [shelterImages, setShelterImages] = useState<File[]>([]);
  const [apii, contextHolder] = notification.useNotification();
  const [plainOptions, setPlainOptions] = useState<Shapes[]>([]);
  const [selectedShape, setSelectedShape] = useState<number | null>(null);

  useEffect(() => {
    const fetchShapes = async () => {
      const shapes = await getAllShapes();
      setPlainOptions(shapes || []);
    };
    fetchShapes();
  }, []);

  const getAllShapes = async (): Promise<Shapes[] | null> => {
    try {
      const response = await api.get('/shapes/getAll-Shapes');
      return response.data.code === 1000
          ? response.data.result.map((shape: any) => ({ id: shape.id, shape: shape.shape }))
          : null;
    } catch (error) {
      console.error("Error fetching shapes: ", error);
      return null;
    }
  };

  const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
    const uploadPromises = files.map(async (file) => {
      const storageRef = ref(storage, `shelter_images/${file.name}`);
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

  const handleSubmit = async (values: any) => {
    const base64Images = shelterImages.length > 0 ? await uploadImagesToFirebase(shelterImages) : [];
    try {
      const response = await api.post("/shelters", {
        shelterCategoryName: values.shelterCategoryName,
        description: values.description,
        shape: { id: selectedShape },
        width: values.width,
        height: values.height,
        length: values.length,
        diameter: values.diameter,
        waterVolume: values.waterVolume,
        waterFiltrationSystem: values.waterFiltrationSystem,
        shelterImages: base64Images.map(url => ({ imageUrl: url })),
      });

      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shelter has been successfully added.' });
        // Optionally reset the form here
      } else {
        apii.error({ message: 'Error', description: response.data.message });
      }
    } catch (error: any) {
      console.error(error); // Log the error for debugging
  
      // Fallback error handling in case `error.response` is not available
      const errorMessage = error.response?.data?.message || error.message || 'An unexpected error occurred.';
      
      apii.error({
        message: 'Error',
        description: errorMessage,
      });
    }
  };

  const handleUploadChange = (info: any) => {
    const { fileList } = info;
    setShelterImages(fileList.map((file: any) => file.originFileObj));
  };

  const validationSchema = Yup.object({
    shelterCategoryName: Yup.string()
        .required("Shelter category name is required")
        .min(3, "Shelter category name must be at least 3 characters")
        .max(50, "Shelter category name must be less than 50 characters")
        .matches(/^[A-Za-z\s]+$/, "Shelter category name cannot contain numbers or special characters"),
    width: Yup.number()
        .required("Width is required")
        .min(1, "Width must be greater than 1")
        .max(20, "Width must be less than or equal to 20 meters"),
    height: Yup.number()
        .required("Height is required")
        .min(1, "Height must be greater than 1")
        .max(10, "Height must be less than or equal to 10 meters"),
    length: Yup.number()
        .required("Length is required")
        .min(1, "Length must be greater than 1")
        .max(50, "Length must be less than or equal to 50 meters"),
    diameter: Yup.number()
        .required("Diameter is required")
        .min(1, "Diameter must be greater than 1")
        .max(20, "Diameter must be less than or equal to 20 meters"),
    waterVolume: Yup.number()
        .required("Water volume is required")
        .min(1, "Water volume must be greater than 1")
        .max(10000, "Water volume must be less than or equal to 10000 cubic meters"),
    waterFiltrationSystem: Yup.string()
        .required("Water filtration system is required")
        .min(3, "Water filtration system must be at least 3 characters")
        .max(100, "Water filtration system must be less than 100 characters"),
    description: Yup.string()
        .max(200, "Description can be up to 200 characters")
  });

  return (
      <div className="container">
        <h1 className="mt-5">Add Shelter</h1>
        <Formik
            initialValues={{
              shelterCategoryName: "",
              description: "",
              width: 0,
              height: 0,
              length: 0,
              diameter: 0,
              waterVolume: 0,
              waterFiltrationSystem: "",
            }}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
        >
          {({ values, handleChange, handleBlur, handleSubmit, errors, touched }) => (
              <Form onFinish={handleSubmit}>
                <Form.Item label="Pond Name" required>
                  <Input
                      name="shelterCategoryName"
                      value={values.shelterCategoryName}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.shelterCategoryName && touched.shelterCategoryName && (
                      <div style={{ color: 'red' }}>{errors.shelterCategoryName}</div>
                  )}
                </Form.Item>

                <Form.Item label="Description" required>
                  <Input
                      name="description"
                      value={values.description}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.description && touched.description && (
                      <div style={{ color: 'red' }}>{errors.description}</div>
                  )}
                </Form.Item>

                <Form.Item label="Width" required>
                  <Input
                      name="width"
                      type="number"
                      value={values.width}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.width && touched.width && (
                      <div style={{ color: 'red' }}>{errors.width}</div>
                  )}
                </Form.Item>

                <Form.Item label="Height" required>
                  <Input
                      name="height"
                      type="number"
                      value={values.height}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.height && touched.height && (
                      <div style={{ color: 'red' }}>{errors.height}</div>
                  )}
                </Form.Item>

                <Form.Item label="Length" required>
                  <Input
                      name="length"
                      type="number"
                      value={values.length}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.length && touched.length && (
                      <div style={{ color: 'red' }}>{errors.length}</div>
                  )}
                </Form.Item>

                <Form.Item label="Diameter" required>
                  <Input
                      name="diameter"
                      type="number"
                      value={values.diameter}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.diameter && touched.diameter && (
                      <div style={{ color: 'red' }}>{errors.diameter}</div>
                  )}
                </Form.Item>

                <Form.Item label="Water Volume" required>
                  <Input
                      name="waterVolume"
                      type="number"
                      value={values.waterVolume}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.waterVolume && touched.waterVolume && (
                      <div style={{ color: 'red' }}>{errors.waterVolume}</div>
                  )}
                </Form.Item>

                <Form.Item label="Water Filtration System" required>
                  <Input
                      name="waterFiltrationSystem"
                      value={values.waterFiltrationSystem}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.waterFiltrationSystem && touched.waterFiltrationSystem && (
                      <div style={{ color: 'red' }}>{errors.waterFiltrationSystem}</div>
                  )}
                </Form.Item>

                <Form.Item label="Shelter Shape">
                  <Radio.Group
                      options={plainOptions.map((option) => ({
                        label: option.shape,
                        value: option.id,
                      }))}
                      onChange={(e) => setSelectedShape(e.target.value)}
                      value={selectedShape}
                  />
                </Form.Item>

                <Form.Item label="Shelter Images">
                  <Upload
                      beforeUpload={(file) => {
                        setShelterImages([file]);
                        return false;
                      }}
                      listType="picture-card"
                      maxCount={5}
                      showUploadList={{ showPreviewIcon: false }}
                      onChange={handleUploadChange}
                  >
                    <div>
                      <PlusOutlined />
                      <div>Upload</div>
                    </div>
                  </Upload>
                </Form.Item>

                <button type="submit" className="btn btn-primary">Submit</button>
              </Form>
          )}
        </Formik>
        {contextHolder}
      </div>
  );
};

export default AddShelter;