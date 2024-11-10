import React, { useEffect, useState } from 'react';
import api from '../../../axious/axious';
import { Table, Button, Modal, Form, Input, Select, notification, Space, Tag } from 'antd';
import { EyeOutlined, EditOutlined, PlusOutlined, SearchOutlined } from '@ant-design/icons';
import { getToken } from '../../../service/localStorageService';

const { Option } = Select;

interface ConsultationShelter {
  id: number;
  description: string;
  consultationResultId: number;
  shelterCategoryId: number;
  status: string;
}

interface ConsultationResult {
  id: number;
  description: string;
  status: string;
}

interface ShelterCategory {
  id: number;
  shelterCategoryName: string;
  description: string;
  origin: string;
  status: string;
  createdDate: string;
  createdBy: string;
  updatedDate: string;
  updatedBy: string;
}

interface ConsultationShelterCollectionProps {
  setIsNavbarVisible: React.Dispatch<React.SetStateAction<boolean>>;
}

const statusOptions = [
  { value: 'PENDING', label: 'Pending' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' },
];

const ConsultationShelterCollection: React.FC<ConsultationShelterCollectionProps> = ({ setIsNavbarVisible }) => {
  const [consultationShelters, setConsultationShelters] = useState<ConsultationShelter[]>([]);
  const [consultationResults, setConsultationResults] = useState<ConsultationResult[]>([]);
  const [shelterCategories, setShelterCategories] = useState<ShelterCategory[]>([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedShelter, setSelectedShelter] = useState<ConsultationShelter | null>(null);
  const [isViewMode, setIsViewMode] = useState(false);
  const [form] = Form.useForm();
  const [searchResultId, setSearchResultId] = useState<number | undefined>(undefined);

  useEffect(() => {
    fetchConsultationShelters();
    fetchConsultationResults();
    fetchShelterCategories();
  }, [searchResultId]);

  const fetchConsultationShelters = async () => {
    setLoading(true);
    const token = getToken();
    try {
      const endpoint = searchResultId
        ? `/api/consultation-shelters/search-by-result-id?resultId=${searchResultId}`
        : '/api/consultation-shelters/shelterCategory';
      const response = await api.get(endpoint, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setConsultationShelters(response.data.result || []);
    } catch (error) {
      notification.error({ message: 'Failed to load consultation shelters' });
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

  const fetchShelterCategories = async () => {
    const token = getToken();
    try {
      const response = await api.get('/shelters/shelterCategory', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setShelterCategories(response.data.result || []);
    } catch (error) {
      notification.error({ message: 'Failed to load shelter categories' });
    }
  };

  const showModal = async (shelter: ConsultationShelter | null = null, viewMode = false) => {
    setSelectedShelter(shelter);
    setIsViewMode(viewMode);
    setIsModalVisible(true);
    setIsNavbarVisible(false);

    if (shelter) {
      form.setFieldsValue({
        description: shelter.description,
        status: shelter.status,
        consultationResultId: shelter.consultationResultId,
        shelterCategoryId: shelter.shelterCategoryId,
      });
    } else {
      form.resetFields();
    }
  };

  const handleFormSubmit = async (values: any) => {
    const token = getToken();
    try {
      if (selectedShelter) {
        await api.put(`/api/consultation-shelters/${selectedShelter.id}`, values, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Updated consultation shelter successfully!' });
      } else {
        await api.post('/api/consultation-shelters', values, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Created consultation shelter successfully!' });
      }
      fetchConsultationShelters();
      setIsModalVisible(false);
      setIsNavbarVisible(true);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Failed to save consultation shelter';
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
      sorter: (a: ConsultationShelter, b: ConsultationShelter) => a.consultationResultId - b.consultationResultId,
    },
    {
      title: 'Shelter Category Name',
      dataIndex: 'shelterCategoryId',
      key: 'shelterCategoryName',
      render: (id: number) => {
        const category = shelterCategories.find(cat => cat.id === id);
        return category ? category.shelterCategoryName : 'N/A';
      },
      width: 200
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
      width: 400
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      sorter: (a: ConsultationShelter, b: ConsultationShelter) => {
        const statusOrder: { [key: string]: number } = { 'PENDING': 1, 'COMPLETED': 2, 'CANCELLED': 3 };
        return statusOrder[a.status as keyof typeof statusOrder] - statusOrder[b.status as keyof typeof statusOrder];
      },
      render: (status: string) => (
        <Tag color={status === 'COMPLETED' ? 'green' : status === 'PENDING' ? 'orange' : 'red'}>
          {status.charAt(0).toUpperCase() + status.slice(1).toLowerCase()}
        </Tag>
      ),
      width: 200
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: any, record: ConsultationShelter) => (
        <Space>
          <Button icon={<EyeOutlined />} onClick={() => showModal(record, true)}>
            View
          </Button>
          <Button icon={<EditOutlined />} type="primary" onClick={() => showModal(record, false)}>
            Edit
          </Button>
        </Space>
      ),
      width: 200
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
          Add Consultation Shelter
        </Button>
      </Space>
      <Table columns={columns} dataSource={consultationShelters} loading={loading} rowKey="id" />

      <Modal
        title={isViewMode ? 'View Consultation Shelter' : selectedShelter ? 'Edit Consultation Shelter' : 'Add Consultation Shelter'}
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
          {isViewMode && selectedShelter && (
            <>
              <p><strong>Consultation Result ID:</strong> {selectedShelter.consultationResultId}</p>
              <p><strong>Shelter Category:</strong> {shelterCategories.find(cat => cat.id === selectedShelter.shelterCategoryId)?.shelterCategoryName || 'N/A'}</p>
            </>
          )}
          {!isViewMode && !selectedShelter && (
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
                label="Shelter Category"
                name="shelterCategoryId"
                rules={[{ required: true, message: 'Please select a shelter category' }]}
              >
                <Select placeholder="Select Shelter Category" disabled={isViewMode}>
                  {shelterCategories.map(category => (
                    <Option key={category.id} value={category.id}>
                      {category.id} - {category.shelterCategoryName} - {category.description}
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

export default ConsultationShelterCollection;
