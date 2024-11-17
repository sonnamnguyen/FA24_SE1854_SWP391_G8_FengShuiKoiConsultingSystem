import React, { useEffect, useState } from 'react';
import api from '../../../axious/axious';
import { notification, Pagination, Table, Modal, Button, Form, Input, Space, Select, Tag } from 'antd';
import { EyeOutlined, EditOutlined } from '@ant-design/icons';
import { getToken } from '../../../service/localStorageService';

const { Option } = Select;

interface ConsultationRequestDetail {
  id: number;
  description: string;
  status: string;
  createdDate: string;
  createdBy: string;
  updatedDate: string;
  updatedBy: string;
  shelterCategoryIds: number[];
  animalCategoryIds: number[];
}

interface ConsultationRequestDetailCollectionProps {
  setIsNavbarVisible: (visible: boolean) => void;
}

const statusOptions = [
  { value: 'PENDING', label: 'Pending' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' },
];

const ConsultationRequestDetailCollection: React.FC<ConsultationRequestDetailCollectionProps> = ({ setIsNavbarVisible }) => {
  const [consultationRequestDetails, setConsultationRequestDetails] = useState<ConsultationRequestDetail[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedDetail, setSelectedDetail] = useState<ConsultationRequestDetail | null>(null);
  const [isViewMode, setIsViewMode] = useState(false);
  const [form] = Form.useForm();
  const pageSize = 10;

  const [expandedDescriptions, setExpandedDescriptions] = useState<{ [key: number]: boolean }>({});

  useEffect(() => {
    fetchConsultationRequestDetails(page, pageSize);
  }, [page]);

  const fetchConsultationRequestDetails = async (page: number, size: number) => {
    setLoading(true);
    const token = getToken();
    try {
      const response = await api.get(`/api/consultation-request-details`, {
        params: { page, size },
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.status === 200 && response.data.result) {
        setConsultationRequestDetails(response.data.result.data);
        setTotalElements(response.data.result.totalElements);
      } else {
        throw new Error('Unexpected API response format');
      }
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Failed to fetch consultation request details.';
      notification.error({ message: 'Error', description: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  const showViewModal = (detail: ConsultationRequestDetail) => {
    setSelectedDetail(detail);
    setIsViewMode(true);
    setIsModalVisible(true);
    setIsNavbarVisible(false);
  };

  const showEditModal = (detail: ConsultationRequestDetail) => {
    setSelectedDetail(detail);
    setIsViewMode(false);
    setIsModalVisible(true);
    setIsNavbarVisible(false);
  };

  const handleModalOk = async () => {
    const token = getToken();
    try {
      if (selectedDetail) {
        const dataToSend = {
          description: selectedDetail.description,
          status: selectedDetail.status,
        };

        if (!dataToSend.description || !dataToSend.status) {
          notification.error({ message: 'Validation Error', description: 'All fields are required.' });
          return;
        }

        await api.put(`/api/consultation-request-details/${selectedDetail.id}`, dataToSend, {
          headers: { Authorization: `Bearer ${token}` },
        });
        notification.success({ message: 'Update successful!' });
        fetchConsultationRequestDetails(page, pageSize);
        setIsModalVisible(false);
        setIsNavbarVisible(true);
      }
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Failed to update consultation request detail.';
      notification.error({ message: 'Error', description: errorMessage });
    }
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    setIsNavbarVisible(true);
  };

  const toggleDescription = (id: number) => {
    setExpandedDescriptions((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  const renderDescription = (text: string, id: number) => {
    const isExpanded = expandedDescriptions[id];
    const maxLength = 50;

    if (text.length <= maxLength) {
      return text;
    }

    return (
      <>
        {isExpanded ? text : `${text.slice(0, maxLength)}... `}
        <a onClick={() => toggleDescription(id)} style={{ color: '#1890ff', cursor: 'pointer' }}>
          {isExpanded ? 'Show less' : 'Show more'}
        </a>
      </>
    );
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      render: (text: string, record: ConsultationRequestDetail) => renderDescription(text, record.id),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      sorter: (a: ConsultationRequestDetail, b: ConsultationRequestDetail) => {
        const statusOrder: { [key: string]: number } = { 'PENDING': 1, 'COMPLETED': 2, 'CANCELLED': 3 };
        return statusOrder[a.status as keyof typeof statusOrder] - statusOrder[b.status as keyof typeof statusOrder];
      },
      render: (status: string) => (
        <Tag color={status === 'COMPLETED' ? 'green' : status === 'PENDING' ? 'orange' : 'red'}>
          {status.charAt(0).toUpperCase() + status.slice(1).toLowerCase()}
        </Tag>
      ),
    },
    {
      title: 'Shelter Categories',
      dataIndex: 'shelterCategoryIds',
      key: 'shelterCategoryIds',
      render: (ids: number[]) => ids.sort((a, b) => a - b).join(', '), // Sort in ascending order
    },
    {
      title: 'Animal Categories',
      dataIndex: 'animalCategoryIds',
      key: 'animalCategoryIds',
      render: (ids: number[]) => ids.sort((a, b) => a - b).join(', '), // Sort in ascending order
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: any, record: ConsultationRequestDetail) => (
        <Space>
          <Button icon={<EyeOutlined />} onClick={() => showViewModal(record)}>
            View
          </Button>
          <Button icon={<EditOutlined />} type="primary" onClick={() => showEditModal(record)}>
            Edit
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Table
        columns={columns}
        dataSource={consultationRequestDetails}
        pagination={false}
        rowKey="id"
        loading={loading}
      />
      <Pagination
        current={page}
        pageSize={pageSize}
        total={totalElements}
        onChange={(newPage) => setPage(newPage)}
        style={{ marginTop: '20px', textAlign: 'center' }}
      />

      <Modal
        title={isViewMode ? 'View Consultation Request Detail' : 'Edit Consultation Request Detail'}
        open={isModalVisible}
        onOk={isViewMode ? handleModalCancel : handleModalOk}
        onCancel={handleModalCancel}
        okText={isViewMode ? 'Close' : 'Save'}
        width={800}
      >
        <Form form={form} layout="vertical">
          {isViewMode && selectedDetail ? (
            <>
              <p><strong>Description:</strong> {renderDescription(selectedDetail.description, selectedDetail.id)}</p>
              <p><strong>Status:</strong> {selectedDetail.status}</p>
              <p><strong>Shelter Categories:</strong> {selectedDetail.shelterCategoryIds.sort((a, b) => a - b).join(', ')}</p>
              <p><strong>Animal Categories:</strong> {selectedDetail.animalCategoryIds.sort((a, b) => a - b).join(', ')}</p>
              <p><strong>Created By:</strong> {selectedDetail.createdBy}</p>
              <p><strong>Created Date:</strong> {selectedDetail.createdDate}</p>
              <p><strong>Updated By:</strong> {selectedDetail.updatedBy}</p>
            </>
          ) : (
            <>
              <Form.Item label="Description" name="description" initialValue={selectedDetail?.description}>
                <Input.TextArea
                  value={selectedDetail?.description}
                  onChange={(e) => setSelectedDetail({ ...selectedDetail!, description: e.target.value })}
                  autoSize={{ minRows: 3, maxRows: 6 }}
                />
              </Form.Item>
              <Form.Item label="Status" name="status" initialValue={selectedDetail?.status}>
                <Select
                  onChange={(status) => setSelectedDetail({ ...selectedDetail!, status })}
                >
                  {statusOptions.map((option) => (
                    <Option key={option.value} value={option.value}>
                      {option.label}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            </>
          )}
        </Form>
      </Modal>
    </div>
  );
};

export default ConsultationRequestDetailCollection;
