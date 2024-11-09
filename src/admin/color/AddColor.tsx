import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../../service/localStorageService";
import { Radio, Form, Input, Button, notification } from 'antd';
import { Formik } from 'formik';
import * as Yup from 'yup'; // Import Yup for validation
import api from "../../axious/axious"; // Ensure axios is configured here

interface Destinys {
  id: number;
  destiny: string;
}

const AddColor: React.FC = () => {
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
      console.error("Failed to fetch destinies: ", response.status);
      return null;
    } catch (error) {
      console.error("Error fetching destinies: ", error);
      return null;
    }
  };

  // Validation schema using Yup (without destiny validation)
  const validationSchema = Yup.object({
    color: Yup.string()
        .required("Color is required")
        .min(3, "Color must be between 3 and 30 characters")
        .max(30, "Color must be between 3 and 30 characters")
        .matches(/^[a-zA-Z\s]+$/, "Color must only contain letters and spaces"), // Allow only letters and spaces
  });

  // Form submission
  const handleSubmit = async (values: any) => {
    // Manually validate destiny
    if (selectedDestiny === null) {
      apii.error({ message: 'Error', description: 'Please select a destiny.' });
      return;
    }

    try {
      const response = await api.post('/colors', {
        color: values.color,
        destiny: { id: selectedDestiny },
      });

      const data = await response.data;
      if (data.code === 1000) {
        apii.success({ message: 'Success', description: 'Color has been successfully added.' });
        // Optionally reset form here
      } else {
        apii.error({ message: 'Error', description: 'Failed to add color.' });
      }
    } catch (error) {
      console.error(error);
      apii.error({ message: 'Error', description: 'Error adding color.' });
    }
  };

  return (
      <div className="container">
        <h1 className="mt-5">Add Color</h1>
        <Formik
            initialValues={{
              color: ""
            }}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
        >
          {({ values, handleChange, handleBlur, handleSubmit, errors, touched }) => (
              <Form onFinish={handleSubmit}>
                <Form.Item label="Color Name" required>
                  <Input
                      name="color"
                      value={values.color}
                      onChange={handleChange}
                      onBlur={handleBlur}
                  />
                  {errors.color && touched.color && (
                      <div style={{ color: 'red' }}>{errors.color}</div>
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

export default AddColor;