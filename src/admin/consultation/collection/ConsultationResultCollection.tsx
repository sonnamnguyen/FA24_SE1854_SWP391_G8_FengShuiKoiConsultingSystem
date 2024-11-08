import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { notification, Pagination, Table, Modal, Button, Form, Input, Space, Select, Tag } from 'antd';
import { SearchOutlined, EyeOutlined, EditOutlined, MailOutlined, PlusOutlined } from '@ant-design/icons';
import { getToken } from '../../../service/localStorageService';

interface ConsultationResultsPageProps {
  setIsNavbarVisible: (visible: boolean) => void;
}

interface ConsultationResult {
  id: number;
  consultantName: string;
  description: string;
  status: string;
  createdDate?: string;
  consultationCategoryId?: number;
  accountId?: number;
  consultationRequestId?: number;
  consultationRequestDetailId?: number;
}

interface ConsultationCategory {
  id: number;
  name: string;
}

interface Account {
  id: number;
  fullName: string;
  email: string;
  phoneNumber: string;
}

interface ConsultationRequest {
  id: number;
  fullName: string;
  description: string;
  createdDate: string;
}

interface ConsultationRequestDetail {
  id: number;
  description: string;
  createdDate: string;
}

interface ApiResponse<T> {
  result: T;
}

interface PageResponse<T> {
  data: T[];
  totalElements: number;
}

const { Option } = Select;

const statusOptions = [
  { value: 'PENDING', label: 'Pending' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' },
];

const ConsultationResultsPage: React.FC<ConsultationResultsPageProps> = ({ setIsNavbarVisible }) => {
  const [consultationResults, setConsultationResults] = useState<ConsultationResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedResult, setSelectedResult] = useState<ConsultationResult | null>(null);
  const [isUpdateMode, setIsUpdateMode] = useState(false);
  const [isViewMode, setIsViewMode] = useState(false);
  const [search, setSearch] = useState('');
  const [accountDetails, setAccountDetails] = useState<Account | null>(null);
  const [consultationRequests, setConsultationRequests] = useState<ConsultationRequest[]>([]);
  const [selectedRequestDetail, setSelectedRequestDetail] = useState<ConsultationRequestDetail | null>(null);
  const [selectedRequestInfo, setSelectedRequestInfo] = useState<ConsultationRequest | null>(null);
  const [consultationCategories, setConsultationCategories] = useState<ConsultationCategory[]>([]);
  const pageSize = 10;

  const fetchConsultationResults = async (page: number, size: number, search: string) => {
    setLoading(true);
    const token = getToken();

    try {
      const response = await axios.get<ApiResponse<PageResponse<ConsultationResult>>>(
        `http://localhost:9090/api/consultation-results/consultation-result-search`, {
          params: { page, size, search },
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (response.status === 200 && response.data.result) {
        setConsultationResults(response.data.result.data);
        setTotalElements(response.data.result.totalElements);
      } else {
        throw new Error('Unexpected API response format');
      }
    } catch (error: any) {
      console.error('Error fetching consultation results:', error);
      notification.error({
        message: 'Error',
        description: 'Failed to fetch consultation results.',
      });
    } finally {
      setLoading(false);
    }
  };

  const fetchConsultationRequests = async () => {
    const token = getToken();
    try {
      const response = await axios.get<ApiResponse<ConsultationRequest[]>>(
        `http://localhost:9090/api/consultation-requests`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 200 && response.data.result) {
        setConsultationRequests(response.data.result);
      }
    } catch (error) {
      console.error('Error fetching consultation requests:', error);
    }
  };

  const fetchConsultationCategories = async () => {
    const token = getToken();
    try {
      const response = await axios.get<ApiResponse<ConsultationCategory[]>>(
        `http://localhost:9090/api/consultation-category`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 200 && response.data.result) {
        setConsultationCategories(response.data.result);
      }
    } catch (error) {
      console.error('Error fetching consultation categories:', error);
    }
  };

  const fetchAccountDetails = async (accountId: number) => {
    const token = getToken();
    try {
      const response = await axios.get<ApiResponse<Account>>(
        `http://localhost:9090/users/${accountId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 200 && response.data.result) {
        setAccountDetails(response.data.result);
      }
    } catch (error) {
      console.error('Error fetching account details:', error);
    }
  };

  const fetchRequestInfo = async (requestId: number) => {
    const token = getToken();
    try {
      const response = await axios.get<ApiResponse<ConsultationRequest>>(
        `http://localhost:9090/api/consultation-requests/${requestId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 200 && response.data.result) {
        setSelectedRequestInfo(response.data.result);
      }
    } catch (error) {
      console.error('Error fetching request information:', error);
    }
  };

  const fetchRequestDetailInfo = async (requestDetailId: number) => {
    const token = getToken();
    try {
      const response = await axios.get<ApiResponse<ConsultationRequestDetail>>(
        `http://localhost:9090/api/consultation-request-details/${requestDetailId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 200 && response.data.result) {
        setSelectedRequestDetail(response.data.result);
      }
    } catch (error) {
      console.error('Error fetching request detail info:', error);
    }
  };

  useEffect(() => {
    fetchConsultationResults(page, pageSize, search);
    fetchConsultationRequests();
    fetchConsultationCategories();
  }, [page, search]);

  const onPageChange = (newPage: number) => {
    setPage(newPage);
  };

  const onSearch = (value: string) => {
    setSearch(value);
    setPage(1);
  };

  const showViewModal = async (result: ConsultationResult) => {
    setSelectedResult(result);
    setIsViewMode(true);
    setIsUpdateMode(false);
    setIsModalVisible(true);
    setIsNavbarVisible(false);

    if (result.accountId) fetchAccountDetails(result.accountId);
    if (result.consultationRequestId) fetchRequestInfo(result.consultationRequestId);
    if (result.consultationRequestDetailId) fetchRequestDetailInfo(result.consultationRequestDetailId);
  };

  const showEditModal = async (result: ConsultationResult) => {
    setSelectedResult(result);
    setIsViewMode(false);
    setIsUpdateMode(true);
    setIsModalVisible(true);
    setIsNavbarVisible(false);

    if (result.accountId) await fetchAccountDetails(result.accountId);
  };

  const showAddModal = () => {
    setSelectedResult({ id: 0, consultantName: '', description: '', status: 'PENDING' });
    setIsViewMode(false);
    setIsUpdateMode(false);
    setIsModalVisible(true);
    setIsNavbarVisible(false);
  };

  const handleRequestSelect = async (requestId: number) => {
    setSelectedResult({ ...selectedResult!, consultationRequestId: requestId });
    await fetchRequestDetailInfo(requestId);
  };

  const handleCategorySelect = (categoryId: number) => {
    setSelectedResult({ ...selectedResult!, consultationCategoryId: categoryId });
  };

  const handleModalOk = async () => {
    if (selectedResult) {
      const token = getToken();
      try {
        if (isUpdateMode && selectedResult.id) {
          await axios.put(
            `http://localhost:9090/api/consultation-results/${selectedResult.id}`,
            {
              consultantName: selectedResult.consultantName,
              description: selectedResult.description,
              status: selectedResult.status,
              consultationCategoryId: selectedResult.consultationCategoryId,
            },
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
          notification.success({ message: 'Update successful!' });
        } else if (!isViewMode) {
          await axios.post(
            `http://localhost:9090/api/consultation-results/requestId/${selectedResult.consultationRequestId}`,
            selectedResult,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
          notification.success({ message: 'Addition successful!' });
        }
        fetchConsultationResults(page, pageSize, search);
      } catch (error) {
        console.error('Error saving consultation result:', error);
        notification.error({ message: 'Error', description: 'Failed to save consultation result.' });
      }
    }
    setIsModalVisible(false);
    setIsNavbarVisible(true);
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    setIsNavbarVisible(true);
  };

  const handleSendEmail = async (resultId: number) => {
    const token = getToken();
    try {
      await axios.put(
        `http://localhost:9090/api/consultation-results/send-email/${resultId}`,
        null,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      notification.success({ message: 'Email sent successfully!' });
    } catch (error) {
      console.error('Error sending email:', error);
      notification.error({
        message: 'Error',
        description: 'Failed to send email.',
      });
    }
  };

  const columns = [
    {
      title: 'Số thứ tự',
      dataIndex: 'index',
      key: 'index',
      render: (_: any, __: ConsultationResult, index: number) => index + 1 + (page - 1) * pageSize,
    },
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Consultant Name',
      dataIndex: 'consultantName',
      key: 'consultantName',
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
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => (
        <Tag color={status === 'COMPLETED' ? 'green' : status === 'PENDING' ? 'orange' : 'red'}>
          {status.charAt(0) + status.slice(1).toLowerCase()}
        </Tag>
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: any, record: ConsultationResult) => (
        <Space>
          <Button icon={<EyeOutlined />} type="default" onClick={() => showViewModal(record)}>
            View
          </Button>
          <Button icon={<EditOutlined />} type="primary" onClick={() => showEditModal(record)}>
            Edit
          </Button>
          <Button icon={<MailOutlined />} type="dashed" onClick={() => handleSendEmail(record.id)}>
            Send Email
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: '20px' }}>
      <h2>Consultation Results</h2>
      <Space style={{ marginBottom: '20px' }}>
        <Input.Search
          placeholder="Search by Consultant Name"
          enterButton={<SearchOutlined />}
          onSearch={onSearch}
          style={{ width: '300px' }}
        />
        <Button type="primary" icon={<PlusOutlined />} onClick={showAddModal}>
          Add Consultation Result
        </Button>
      </Space>
      <Table
        columns={columns}
        dataSource={consultationResults}
        pagination={false}
        rowKey="id"
        loading={loading}
      />
      <Pagination
        current={page}
        pageSize={pageSize}
        total={totalElements}
        onChange={onPageChange}
        style={{ marginTop: '20px', textAlign: 'center' }}
      />

      <Modal
        title={isViewMode ? "View Consultation Result" : isUpdateMode ? "Edit Consultation Result" : "Add Consultation Result"}
        visible={isModalVisible}
        onOk={isViewMode ? handleModalCancel : handleModalOk}
        onCancel={handleModalCancel}
        okText={isViewMode ? "Close" : "Save"}
        cancelButtonProps={{ style: { display: isViewMode ? 'none' : 'inline-block' } }}
      >
        <Form layout="vertical">
          {isUpdateMode || isViewMode ? (
            <>
              <Form.Item label="Account Details">
                <p><strong>ID:</strong> {accountDetails?.id}</p>
                <p><strong>Name:</strong> {accountDetails?.fullName}</p>
                <p><strong>Email:</strong> {accountDetails?.email}</p>
                <p><strong>Phone Number:</strong> {accountDetails?.phoneNumber}</p>
              </Form.Item>
              <Form.Item label="Consultation Request Details">
                <p><strong>ID:</strong> {selectedRequestInfo?.id}</p>
                <p><strong>Description:</strong> {selectedRequestInfo?.description}</p>
                <p><strong>Created Date:</strong> {selectedRequestInfo?.createdDate}</p>
              </Form.Item>
              <Form.Item label="Consultation Request Detail">
                <p><strong>ID:</strong> {selectedRequestDetail?.id}</p>
                <p><strong>Description:</strong> {selectedRequestDetail?.description}</p>
                <p><strong>Created Date:</strong> {selectedRequestDetail?.createdDate}</p>
              </Form.Item>
            </>
          ) : (
            <>
              <Form.Item label="Consultation Request">
                <Select
                  placeholder="Select Consultation Request"
                  onChange={handleRequestSelect}
                  value={selectedResult?.consultationRequestId}
                >
                  {consultationRequests.map((request) => (
                    <Option key={request.id} value={request.id}>
                      {request.id} - {request.fullName} - {request.createdDate}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item label="Consultation Category">
                <Select
                  placeholder="Select Consultation Category"
                  onChange={handleCategorySelect}
                  value={selectedResult?.consultationCategoryId}
                >
                  {consultationCategories.map((category) => (
                    <Option key={category.id} value={category.id}>
                      {category.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            </>
          )}
          <Form.Item label="Consultant Name">
            <Input
              value={selectedResult?.consultantName}
              onChange={(e) => setSelectedResult({ ...selectedResult!, consultantName: e.target.value })}
              disabled={isViewMode}
            />
          </Form.Item>
          <Form.Item label="Description">
            <Input
              value={selectedResult?.description}
              onChange={(e) => setSelectedResult({ ...selectedResult!, description: e.target.value })}
              disabled={isViewMode}
            />
          </Form.Item>
          <Form.Item label="Status">
            <Select
              value={selectedResult?.status}
              onChange={(value) => setSelectedResult({ ...selectedResult!, status: value })}
              disabled={isViewMode}
            >
              {statusOptions.map(option => (
                <Option key={option.value} value={option.value}>
                  {option.label}
                </Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ConsultationResultsPage;
