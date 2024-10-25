import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../../service/localStorageService";
import { Radio, Form, Input, Button, notification } from 'antd';
import Destiny from "../../models/Destiny";
import api from "../../axious/axious"; // Ensure axios is configured here

interface Destinys {
  id: number;
  destiny: string;
}

function AddColor() {
  const [color, setColor] = useState("");
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

  // Form submission
  const handleSubmit = async () => {
    try {
      const response = await api.post('/colors', {
        color,
        destiny: { id: selectedDestiny },
      });

      const data = await response.data;
      if (data.code === 1000) {
        apii.success({ message: 'Success', description: 'Color has been successfully added.' });
        setColor("");
        setSelectedDestiny(null);
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
      <Form onFinish={handleSubmit}>
        <Form.Item label="Color Name" required rules={[{ required: true, message: 'Please input the color name!' }]}>
          <Input value={color} onChange={(e) => setColor(e.target.value)} />
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
      {contextHolder}
    </div>
  );
}

export default AddColor;
