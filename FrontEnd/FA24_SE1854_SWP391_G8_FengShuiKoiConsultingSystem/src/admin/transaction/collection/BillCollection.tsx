import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Select, notification, Space, Tag, Input } from 'antd';
import { EyeOutlined } from '@ant-design/icons';
import api from '../../../axious/axious'; // Đảm bảo đường dẫn đến axios instance đúng
import { getToken } from '../../../service/localStorageService';

const { Option } = Select;

interface Payment {
  id: number;
  paymentMethod: string;
}

interface Bill {
  id: number;
  accountId: number | null;
  totalAmount: number;
  status: string;
  createdDate: string;
  createdBy: string;
  payment: Payment;
}

interface ApiResponse<T> {
  result: T;
}

interface BillCollectionProps {
  setIsNavbarVisible: (visible: boolean) => void;
}

const BillCollection: React.FC<BillCollectionProps> = ({ setIsNavbarVisible }) => {
  const [bills, setBills] = useState<Bill[]>([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedBill, setSelectedBill] = useState<Bill | null>(null);

  // State cho các điều kiện tìm kiếm
  const [search, setSearch] = useState<{
    createdBy: string;
    status: string;
    paymentMethod: string;
    minTotalAmount: number | null;
    maxTotalAmount: number | null;
  }>({
    createdBy: '',
    status: '',
    paymentMethod: '',
    minTotalAmount: null,
    maxTotalAmount: null,
  });

  const statusOptions = [
    { value: 'PENDING', label: 'Pending' },
    { value: 'PAID', label: 'Paid' },
    { value: 'CANCELLED', label: 'Cancelled' },
  ];

  const paymentOptions = [
    { value: 'VNPay', label: 'VNPay' },
    { value: 'PayPal', label: 'PayPal' },
  ];

  useEffect(() => {
    fetchBills();
  }, [search]);

  // Hàm fetch bills sử dụng endpoint tìm kiếm
  const fetchBills = async () => {
    setLoading(true);
    const token = getToken();

    // Thiết lập các tham số tìm kiếm dựa trên state
    const params: any = {
      ...(search.status && { status: search.status }),
      ...(search.createdBy && { createdBy: search.createdBy }),
      ...(search.minTotalAmount !== null ? { minTotalAmount: search.minTotalAmount } : {}),
      ...(search.maxTotalAmount !== null ? { maxTotalAmount: search.maxTotalAmount } : {}),
      ...(search.paymentMethod && { paymentMethod: search.paymentMethod }),
    };

    try {
      console.log('Params being sent to search bills:', params);
      const response = await api.get<ApiResponse<Bill[]>>(`/api/bills/search`, {
        headers: { Authorization: `Bearer ${token}` },
        params,
      });

      console.log('API Response:', response);

      // Lấy mảng `bills` từ `response.data.result`
      const data = response.data.result || [];
      setBills(data);
      console.log('Bills:', data); // Log để xác nhận dữ liệu đã được thiết lập
    } catch (error) {
      console.error('Error fetching bills:', error);
      notification.error({ message: 'Error fetching bills' });
    } finally {
      setLoading(false);
    }
  };

  const handleViewBill = (bill: Bill) => {
    setSelectedBill(bill);
    setIsModalVisible(true);
    setIsNavbarVisible(false);
  };

  const handleSearch = (field: keyof typeof search, value: any) => {
    setSearch((prevSearch) => ({ ...prevSearch, [field]: value }));
  };

  const applySearch = () => {
    const { minTotalAmount, maxTotalAmount } = search;

    if (minTotalAmount !== null && minTotalAmount < 1000) {
      notification.error({ message: 'Min Total Amount must be >= 1000' });
      return;
    }
    if (maxTotalAmount !== null && maxTotalAmount < (minTotalAmount || 0)) {
      notification.error({ message: 'Max Total Amount must be greater than or equal to Min Total Amount' });
      return;
    }
    fetchBills();
  };

  const clearSearch = () => {
    setSearch({ createdBy: '', status: '', paymentMethod: '', minTotalAmount: null, maxTotalAmount: null });
    fetchBills();
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: 'Created By', dataIndex: 'createdBy', key: 'createdBy' },
    { title: 'Total Amount', dataIndex: 'totalAmount', key: 'totalAmount' },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => (
        <Tag color={status === 'PAID' ? 'green' : status === 'PENDING' ? 'orange' : 'red'}>
          {status}
        </Tag>
      ),
    },
    { title: 'Created Date', dataIndex: 'createdDate', key: 'createdDate' },
    {
      title: 'Payment Method',
      dataIndex: ['payment', 'paymentMethod'],
      key: 'paymentMethod',
      render: (paymentMethod: string) => paymentMethod || 'N/A',
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: any, record: Bill) => (
        <Space>
          <Button icon={<EyeOutlined />} onClick={() => handleViewBill(record)}>
            View
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h2>Bill Management</h2>
      <Space style={{ marginBottom: 16 }}>
        <Input
          placeholder="Filter by Created By"
          value={search.createdBy}
          onChange={(e) => handleSearch('createdBy', e.target.value)}
        />
        <Select
          placeholder="Filter by Status"
          value={search.status || null}
          onChange={(value) => handleSearch('status', value)}
          style={{ width: '200px' }}
        >
          {statusOptions.map((option) => (
            <Option key={option.value} value={option.value}>
              {option.label}
            </Option>
          ))}
        </Select>
        <Input
          placeholder="Min Total Amount"
          value={search.minTotalAmount || ''}
          type="number"
          onChange={(e) => handleSearch('minTotalAmount', e.target.value ? parseInt(e.target.value) : null)}
          style={{ width: '120px' }}
        />
        <Input
          placeholder="Max Total Amount"
          value={search.maxTotalAmount || ''}
          type="number"
          onChange={(e) => handleSearch('maxTotalAmount', e.target.value ? parseInt(e.target.value) : null)}
          style={{ width: '120px' }}
        />
        <Select
          placeholder="Select Payment Method"
          value={search.paymentMethod || null}
          onChange={(value) => handleSearch('paymentMethod', value)}
          style={{ width: '200px' }}
        >
          {paymentOptions.map((option) => (
            <Option key={option.value} value={option.value}>
              {option.label}
            </Option>
          ))}
        </Select>
        <Button type="primary" onClick={applySearch}>
          Search
        </Button>
        <Button onClick={clearSearch}>Clear Filters</Button>
      </Space>
      <Table columns={columns} dataSource={bills} loading={loading} rowKey="id" />

      <Modal
        title="Bill Details"
        open={isModalVisible}
        onCancel={() => {
          setIsModalVisible(false);
          setIsNavbarVisible(true);
        }}
        footer={null}
        width={800}
      >
        {selectedBill && (
          <div>
            <p><strong>Bill ID:</strong> {selectedBill.id}</p>
            <p><strong>Created By:</strong> {selectedBill.createdBy}</p>
            <p><strong>Total Amount:</strong> {selectedBill.totalAmount}</p>
            <p><strong>Status:</strong> {selectedBill.status}</p>
            <p><strong>Created Date:</strong> {selectedBill.createdDate}</p>
            <p><strong>Payment Method:</strong> {selectedBill.payment.paymentMethod}</p>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default BillCollection;
