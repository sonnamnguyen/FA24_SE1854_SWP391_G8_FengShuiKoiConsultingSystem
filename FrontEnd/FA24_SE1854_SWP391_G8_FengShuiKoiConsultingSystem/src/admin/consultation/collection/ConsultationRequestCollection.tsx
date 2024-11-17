import React, { useEffect, useState } from 'react';
import api from '../../../axious/axious';
import { Table, Button, Modal, Form, Select, notification, Space, Tag, Input } from 'antd';
import { EyeOutlined, EditOutlined, SearchOutlined } from '@ant-design/icons';
import { getToken } from '../../../service/localStorageService';

const { Option } = Select;

interface ConsultationRequest {
  id: number;
  fullName: string;
  email: string;
  phone: string;
  status: string;
  packageId: number;
  gender: string;
}

interface ConsultationRequestCollectionProps {
  setIsNavbarVisible: React.Dispatch<React.SetStateAction<boolean>>;
}

const statusOptions = [
  { value: 'PENDING', label: 'Pending' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' },
];

const ConsultationRequestCollection: React.FC<ConsultationRequestCollectionProps> = ({ setIsNavbarVisible }) => {
  const [consultationRequests, setConsultationRequests] = useState<ConsultationRequest[]>([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState<ConsultationRequest | null>(null);
  const [isViewMode, setIsViewMode] = useState(false);
  const [form] = Form.useForm();
  const [search, setSearch] = useState({ fullName: '', email: '', phone: '' });
  const [disabledFields, setDisabledFields] = useState({ fullName: false, email: false, phone: false });

  useEffect(() => {
    fetchConsultationRequests();
  }, [search]);

  const fetchConsultationRequests = async () => {
    setLoading(true);
    const token = getToken();
    try {
      const response = await api.get(`/api/consultation-requests/search`, {
        headers: { Authorization: `Bearer ${token}` },
        params: {
          fullName: search.fullName || undefined,
          email: search.email || undefined,
          phone: search.phone || undefined,
        },
      });
      setConsultationRequests(response.data.result || []);
    } catch (error) {
      notification.error({ message: 'Failed to load consultation requests' });
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (field: string, value: string) => {
    setSearch((prevSearch) => ({ ...prevSearch, [field]: value }));
  };

  const executeSearch = (field: string, value: string) => {
    setSearch({ fullName: '', email: '', phone: '', [field]: value });
    setDisabledFields({
      fullName: field !== 'fullName',
      email: field !== 'email',
      phone: field !== 'phone',
    });
  };

  const clearSearch = () => {
    setSearch({ fullName: '', email: '', phone: '' });
    setDisabledFields({ fullName: false, email: false, phone: false });
    fetchConsultationRequests(); // Refresh data after clearing search
  };

  const showModal = async (request: ConsultationRequest | null = null, viewMode = false) => {
    setSelectedRequest(request);
    setIsViewMode(viewMode);
    setIsModalVisible(true);
    setIsNavbarVisible(false);

    if (request) {
      form.setFieldsValue({
        status: request.status,
      });
    } else {
      form.resetFields();
    }
  };

  const handleFormSubmit = async (values: any) => {
    const token = getToken();
    try {
      if (selectedRequest) {
        await api.put(`/api/consultation-requests/${selectedRequest.id}`, { status: values.status }, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Updated consultation request status successfully!' });
        fetchConsultationRequests();
      }
      setIsModalVisible(false);
      setIsNavbarVisible(true);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Failed to update consultation request status';
      notification.error({ message: 'Error', description: errorMessage });
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 50 },
    { title: 'Full Name', dataIndex: 'fullName', key: 'fullName', width: 200 },
    { title: 'Email', dataIndex: 'email', key: 'email', width: 200 },
    { title: 'Phone', dataIndex: 'phone', key: 'phone', width: 150 },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      sorter: (a: ConsultationRequest, b: ConsultationRequest) => {
        const statusOrder: { [key: string]: number } = { 'PENDING': 1, 'COMPLETED': 2, 'CANCELLED': 3 };
        return statusOrder[a.status as keyof typeof statusOrder] - statusOrder[b.status as keyof typeof statusOrder];
      },
      render: (status: string) => (
        <Tag color={status === 'COMPLETED' ? 'green' : status === 'PENDING' ? 'orange' : 'red'}>
          {status.charAt(0).toUpperCase() + status.slice(1).toLowerCase()}
        </Tag>
      ),
      width: 100,
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: any, record: ConsultationRequest) => (
        <Space>
          <Button icon={<EyeOutlined />} onClick={() => showModal(record, true)}>
            View
          </Button>
          <Button icon={<EditOutlined />} type="primary" onClick={() => showModal(record, false)}>
            Edit
          </Button>
        </Space>
      ),
      width: 150,
    },
  ];

  return (
    <div>
      <Space style={{ marginBottom: 16 }}>
        <Input.Search
          placeholder="Search by Full Name"
          enterButton={<SearchOutlined />}
          onSearch={(value) => executeSearch('fullName', value)}
          value={search.fullName}
          style={{ width: 300 }}
          disabled={disabledFields.fullName}
          onChange={(e) => handleSearch('fullName', e.target.value)}
        />
        <Input.Search
          placeholder="Search by Email"
          enterButton={<SearchOutlined />}
          onSearch={(value) => executeSearch('email', value)}
          value={search.email}
          style={{ width: 300 }}
          disabled={disabledFields.email}
          onChange={(e) => handleSearch('email', e.target.value)}
        />
        <Input.Search
          placeholder="Search by Phone"
          enterButton={<SearchOutlined />}
          onSearch={(value) => executeSearch('phone', value)}
          value={search.phone}
          style={{ width: 300 }}
          disabled={disabledFields.phone}
          onChange={(e) => handleSearch('phone', e.target.value)}
        />
        <Button onClick={clearSearch}>Clear</Button>
      </Space>
      <Table columns={columns} dataSource={consultationRequests} loading={loading} rowKey="id" />

      <Modal
        title={isViewMode ? 'View Consultation Request' : 'Edit Consultation Request'}
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
          {isViewMode && selectedRequest && (
            <>
              <p><strong>Full Name:</strong> {selectedRequest.fullName}</p>
              <p><strong>Email:</strong> {selectedRequest.email}</p>
              <p><strong>Phone:</strong> {selectedRequest.phone}</p>
              <p><strong>Status:</strong> {selectedRequest.status}</p>
              <p><strong>Package ID:</strong> {selectedRequest.packageId}</p>
              <p><strong>Gender:</strong> {selectedRequest.gender}</p>
            </>
          )}
          {!isViewMode && (
            <Form.Item label="Status" name="status" rules={[{ required: true, message: 'Please select a status' }]}>
              <Select placeholder="Select Status">
                {statusOptions.map(option => (
                  <Option key={option.value} value={option.value}>
                    {option.label}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          )}
        </Form>
      </Modal>
    </div>
  );
};

export default ConsultationRequestCollection;
