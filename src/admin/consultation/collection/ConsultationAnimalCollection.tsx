import React, { useEffect, useState } from 'react';
import api from '../../../axious/axious';
import { Table, Button, Modal, Form, Input, Select, notification, Space, Tag } from 'antd';
import { EyeOutlined, EditOutlined, PlusOutlined, SearchOutlined } from '@ant-design/icons';
import { getToken } from '../../../service/localStorageService';

const { Option } = Select;

interface ConsultationAnimal {
  id: number;
  description: string;
  consultationResultId: number;
  animalCategoryId: number;
  status: string;
}

interface ConsultationResult {
  id: number;
  description: string;
  status: string;
}

interface AnimalCategory {
  id: number;
  animalCategoryName: string;
  description: string;
  origin: string;
  status: string;
  createdDate: string;
  createdBy: string;
  updatedDate: string;
  updatedBy: string;
}

interface ConsultationAnimalCollectionProps {
  setIsNavbarVisible: React.Dispatch<React.SetStateAction<boolean>>;
}

const statusOptions = [
  { value: 'PENDING', label: 'Pending' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' },
];

const ConsultationAnimalCollection: React.FC<ConsultationAnimalCollectionProps> = ({ setIsNavbarVisible }) => {
  const [consultationAnimals, setConsultationAnimals] = useState<ConsultationAnimal[]>([]);
  const [consultationResults, setConsultationResults] = useState<ConsultationResult[]>([]);
  const [animalCategories, setAnimalCategories] = useState<AnimalCategory[]>([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedAnimal, setSelectedAnimal] = useState<ConsultationAnimal | null>(null);
  const [isViewMode, setIsViewMode] = useState(false);
  const [form] = Form.useForm();
  const [searchResultId, setSearchResultId] = useState<number | undefined>(undefined);

  useEffect(() => {
    fetchConsultationAnimals();
    fetchConsultationResults();
    fetchAnimalCategories();
  }, [searchResultId]);

  const fetchConsultationAnimals = async () => {
    setLoading(true);
    const token = getToken();
    try {
      const endpoint = searchResultId
        ? `/api/consultation-animals/search-by-result-id?resultId=${searchResultId}`
        : '/api/consultation-animals/animalCategory';
      const response = await api.get(endpoint, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setConsultationAnimals(response.data.result || []);
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
      const results = response.data.result?.filter((res: ConsultationResult) => res.status === 'PENDING') || [];
      setConsultationResults(results);
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
      setAnimalCategories(response.data.result || []);
    } catch (error) {
      notification.error({ message: 'Failed to load animal categories' });
    }
  };

  const showModal = async (animal: ConsultationAnimal | null = null, viewMode = false) => {
    setSelectedAnimal(animal);
    setIsViewMode(viewMode);
    setIsModalVisible(true);
    setIsNavbarVisible(false);

    if (animal) {
      form.setFieldsValue({
        description: animal.description,
        status: animal.status,
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
        await api.put(`/api/consultation-animals/${selectedAnimal.id}`, values, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Updated consultation animal successfully!' });
      } else {
        await api.post('/api/consultation-animals', values, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Created consultation animal successfully!' });
      }
      fetchConsultationAnimals();
      setIsModalVisible(false);
      setIsNavbarVisible(true);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Failed to save consultation animal';
      notification.error({ message: 'Error', description: errorMessage });
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 50 },
    {
      title: 'Result ID',
      dataIndex: 'consultationResultId',
      key: 'consultationResultId',
      width: 100,
      sorter: (a: ConsultationAnimal, b: ConsultationAnimal) => a.consultationResultId - b.consultationResultId,
    },
    {
      title: 'Animal Category Name',
      dataIndex: 'animalCategoryId',
      key: 'animalCategoryName',
      render: (id: number) => {
        const category = animalCategories.find(cat => cat.id === id);
        return category ? category.animalCategoryName : 'N/A';
      },
      width: 200,
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      render: (text: string) => (
        <span style={{ display: 'block', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: '200px' }}>
          {text}
        </span>
      ),
      width: 400,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      sorter: (a: ConsultationAnimal, b: ConsultationAnimal) => {
        const statusOrder: { [key: string]: number } = { 'PENDING': 1, 'COMPLETED': 2, 'CANCELLED': 3 };
        return statusOrder[a.status as keyof typeof statusOrder] - statusOrder[b.status as keyof typeof statusOrder];
      },
      render: (status: string) => (
        <Tag color={status === 'COMPLETED' ? 'green' : status === 'PENDING' ? 'orange' : 'red'}>
          {status.charAt(0).toUpperCase() + status.slice(1).toLowerCase()}
        </Tag>
      ),
      width: 200,
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: any, record: ConsultationAnimal) => (
        <Space>
          <Button icon={<EyeOutlined />} onClick={() => showModal(record, true)}>
            View
          </Button>
          <Button icon={<EditOutlined />} type="primary" onClick={() => showModal(record, false)}>
            Edit
          </Button>
        </Space>
      ),
      width: 200,
    },
  ];

  return (
    <div>
      <Space style={{ marginBottom: 16 }}>
        <Input.Search
          placeholder="Search by Result ID"
          enterButton={<SearchOutlined />}
          onSearch={(value) => setSearchResultId(Number(value))}
          style={{ width: 300 }}
        />
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => showModal()}
          style={{ marginLeft: 16 }}
        >
          Add Consultation Animal
        </Button>
      </Space>
      <Table columns={columns} dataSource={consultationAnimals} loading={loading} rowKey="id" />

      <Modal
        title={isViewMode ? 'View Consultation Animal' : selectedAnimal ? 'Edit Consultation Animal' : 'Add Consultation Animal'}
        open={isModalVisible}
        onCancel={() => {
          setIsModalVisible(false);
          setIsNavbarVisible(true);
        }}
        onOk={() => !isViewMode && form.submit()}
        okText={isViewMode ? 'Close' : 'Save'}
        okButtonProps={{ disabled: isViewMode }}
        cancelButtonProps={{ style: { display: isViewMode ? 'none' : 'inline-block' } }}
        width={800}
      >
        <Form form={form} layout="vertical" onFinish={handleFormSubmit}>
          {isViewMode && selectedAnimal && (
            <>
              <p><strong>Consultation Result ID:</strong> {selectedAnimal.consultationResultId}</p>
              <p><strong>Animal Category:</strong> {animalCategories.find(cat => cat.id === selectedAnimal.animalCategoryId)?.animalCategoryName || 'N/A'}</p>
            </>
          )}
          {!isViewMode && !selectedAnimal && (
            <>
              <Form.Item label="Consultation Result ID" name="consultationResultId" rules={[{ required: true, message: 'Please select a consultation result' }]}>
                <Select placeholder="Select Consultation Result" disabled={isViewMode}>
                  {consultationResults.map(result => (
                    <Option key={result.id} value={result.id}>
                      {result.id} - {result.description}
                    </Option>
                  ))}
                </Select>
              </Form.Item>

              <Form.Item
                label="Animal Category"
                name="animalCategoryId"
                rules={[{ required: true, message: 'Please select an animal category' }]}
              >
                <Select placeholder="Select Animal Category" disabled={isViewMode}>
                  {animalCategories.map(category => (
                    <Option key={category.id} value={category.id}>
                      {category.id} - {category.animalCategoryName} - {category.description}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            </>
          )}

          <Form.Item
            label="Status"
            name="status"
            rules={[{ required: true, message: 'Please select a status' }]}
          >
            <Select placeholder="Select Status" disabled={isViewMode}>
              {statusOptions.map(option => (
                <Option key={option.value} value={option.value}>
                  {option.label}
                </Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            label="Description"
            name="description"
            rules={[{ required: true, message: 'Please enter a description' }]}
          >
            <Input.TextArea
              disabled={isViewMode}
              autoSize={{ minRows: 3, maxRows: 10 }}
              placeholder="Enter description"
            />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ConsultationAnimalCollection;
