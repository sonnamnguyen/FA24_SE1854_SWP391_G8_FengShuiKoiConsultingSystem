import React, { useEffect, useState } from 'react';
import api from '../../../axious/axious';
import { Table, Button, Modal, Form, Input, Select, notification, Space } from 'antd';
import { getToken } from '../../../service/localStorageService';

const { Option } = Select;

interface ConsultationAnimalCollectionProps {
  setIsNavbarVisible: React.Dispatch<React.SetStateAction<boolean>>;
}

interface ConsultationAnimal {
  id: number;
  description: string;
  consultationResultId: number;
  animalCategoryId: number;
}

interface ConsultationResult {
  id: number;
  description: string;
}

interface AnimalCategory {
  id: number;
  name: string;
}

const ConsultationAnimalCollection: React.FC<ConsultationAnimalCollectionProps> = ({ setIsNavbarVisible }) => {
  const [consultationAnimals, setConsultationAnimals] = useState<ConsultationAnimal[]>([]);
  const [consultationResults, setConsultationResults] = useState<ConsultationResult[]>([]);
  const [animalCategories, setAnimalCategories] = useState<AnimalCategory[]>([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedAnimal, setSelectedAnimal] = useState<ConsultationAnimal | null>(null);
  const [form] = Form.useForm();

  useEffect(() => {
    fetchConsultationAnimals();
    fetchConsultationResults();
    fetchAnimalCategories();
  }, []);

  const fetchConsultationAnimals = async () => {
    setLoading(true);
    const token = getToken();
    try {
      const response = await api.get('/api/consultation-animals/animalCategory', {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log("Response fetchConsultationAnimals :", response);
      setConsultationAnimals(response.data.result || []); // Default to an empty array if data is undefined
    } catch (error) {
      notification.error({ message: 'Failed to load consultation animals' });
    } finally {
      setLoading(false);
    }
  };

  const fetchConsultationResults = async () => {
    const token = getToken();
    try {
      const response = await api.get('/api/consultation-results/find-all', {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log("Response fetchConsultationResults :", response);
      setConsultationResults(response.data.result || []);
    } catch (error) {
      notification.error({ message: 'Failed to load consultation results' });
    }
  };

  const fetchAnimalCategories = async () => {
    const token = getToken();
    try {
      const response = await api.get('/animals/animalCategory', {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log("Response fetchAnimalCategories :", response);
      setAnimalCategories(response.data.result || []);
    } catch (error) {
      notification.error({ message: 'Failed to load animal categories' });
    }
  };

  const showModal = (animal: ConsultationAnimal | null = null) => {
    setSelectedAnimal(animal);
    setIsModalVisible(true);
    setIsNavbarVisible(false);
    if (animal) {
      form.setFieldsValue({
        description: animal.description,
        consultationResultId: animal.consultationResultId,
        animalCategoryId: animal.animalCategoryId,
      });
    } else {
      form.resetFields();
    }
  };

  const handleFormSubmit = async (values: any) => {
    const token = getToken();
    try {
      if (selectedAnimal) {
        await api.put(`http://localhost:9090/api/consultation-animals/${selectedAnimal.id}`, values, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Updated consultation animal successfully!' });
      } else {
        await api.post(`http://localhost:9090/api/consultation-animals/resultId/${values.consultationResultId}/animal-category-id/${values.animalCategoryId}`, values, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Created consultation animal successfully!' });
      }
      fetchConsultationAnimals();
      setIsModalVisible(false);
      setIsNavbarVisible(true);
    } catch (error) {
      notification.error({ message: 'Failed to save consultation animal' });
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: 'Description', dataIndex: 'description', key: 'description' },
    { title: 'Consultation Result ID', dataIndex: 'consultationResultId', key: 'consultationResultId' },
    { title: 'Animal Category ID', dataIndex: 'animalCategoryId', key: 'animalCategoryId' },
    {
      title: 'Actions',
      key: 'actions',
      render: (text: any, record: ConsultationAnimal) => (
        <Space>
          <Button onClick={() => showModal(record)}>Edit</Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Button type="primary" onClick={() => showModal()} style={{ marginBottom: 16 }}>
        Add Consultation Animal
      </Button>
      <Table columns={columns} dataSource={consultationAnimals || []} loading={loading} rowKey="id" />
      
      <Modal
        title={selectedAnimal ? 'Edit Consultation Animal' : 'Add Consultation Animal'}
        visible={isModalVisible}
        onCancel={() => {
          setIsModalVisible(false);
          setIsNavbarVisible(true);
        }}
        onOk={() => form.submit()}
      >
        <Form form={form} layout="vertical" onFinish={handleFormSubmit}>
          <Form.Item
            label="Description"
            name="description"
            rules={[{ required: true, message: 'Please enter a description' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Consultation Result ID"
            name="consultationResultId"
            rules={[{ required: true, message: 'Please select a consultation result' }]}
          >
            <Select placeholder="Select Consultation Result">
              {consultationResults?.map(result => (
                <Option key={result.id} value={result.id}>
                  {result.id} - {result.description}
                </Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            label="Animal Category ID"
            name="animalCategoryId"
            rules={[{ required: true, message: 'Please select an animal category' }]}
          >
            <Select placeholder="Select Animal Category">
              {animalCategories?.map(category => (
                <Option key={category.id} value={category.id}>
                  {category.name}
                </Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ConsultationAnimalCollection;
