import React, { useEffect, useState } from "react";
import { Radio, Form, Input, Button, notification } from 'antd';
import { Formik } from 'formik';
import * as Yup from 'yup'; // Import Yup for validation
import api from "../../axious/axious";

interface Destinys {
  id: number;
  destiny: string;
}

const AddShape: React.FC = () => {
  const [apii, contextHolder] = notification.useNotification();
  const [destinyOptions, setDestinyOptions] = useState<Destinys[]>([]);
  const [selectedDestiny, setSelectedDestiny] = useState<number | null>(null);

  useEffect(() => {
    const fetchDestinies = async () => {
      const destinies = await getAllDestinies();
      if (destinies) {
        setDestinyOptions(destinies);
      }
    };
    fetchDestinies();
  }, []);

  const getAllDestinies = async (): Promise<Destinys[] | null> => {
    try {
      const response = await api.get(`/destinys`);
      if (response.data.code === 1000) {
        return response.data.result.map((destiny: any) => ({
          id: destiny.id,
          destiny: destiny.destiny,
        })) as Destinys[];
      }
      console.error("Failed to fetch destinies: ", response.data.message);
      return null;
    } catch (error) {
      console.error("Error fetching destinies: ", error);
      return null;
    }
  };

  // Validation schema using Yup
  const validationSchema = Yup.object({
    shape: Yup.string()
        .required("Shape is required")
        .min(3, "Shape must be between 3 and 50 characters")
        .max(50, "Shape must be between 3 and 50 characters")
        .matches(/^[a-zA-Z\s]+$/, "Shape must only contain letters and spaces"), // Allow only letters and spaces
  });

  const handleSubmit = async (values: any) => {
    try {
      // API call to add a shape
      const response = await api.post('/shapes', {
        shape: values.shape,
        destiny: { id: selectedDestiny },
      });
  
      const data = response.data;
  
      // Check if the response code is successful
      if (data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shape has been successfully added.' });
        // Optionally reset form here
      } else {
        apii.error({ message: 'Error', description: data.message });
      }
    } catch (error: any) {
      console.error(error);
  
      // In case of an error, display a generic error message
      apii.error({
        message: 'Error',
        description: error.response?.data?.message || error.message || 'An unexpected error occurred.',
      });
    }
  };

  return (
      <div className="container">
        <h1 className="mt-5">Add Shape</h1>
        <Formik
            initialValues={{
              shape: "",
              destiny: "",
            }}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
        >
          {({ values, handleChange, handleBlur, handleSubmit, errors, touched }) => (
              <Form onFinish={handleSubmit}>
                <Form.Item label="Shape Name" required>
                  <Input
                      name="shape"
                      value={values.shape}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.shape && touched.shape && (
                      <div style={{ color: 'red' }}>{errors.shape}</div>
                  )}
                </Form.Item>

                <Form.Item label="Destiny">
          <Radio.Group
            onChange={e => setSelectedDestiny(e.target.value)}
            value={selectedDestiny}
          >
            {destinyOptions.map(destiny => (
              <Radio key={destiny.id} value={destiny.id}>
                {destiny.destiny}
              </Radio>
            ))}
          </Radio.Group>
        </Form.Item>

                <Form.Item>
                  <Button type="primary" htmlType="submit">
                    Submit
                  </Button>
                </Form.Item>
              </Form>
          )}
        </Formik>
        {contextHolder}
      </div>
  );
};

export default AddShape;