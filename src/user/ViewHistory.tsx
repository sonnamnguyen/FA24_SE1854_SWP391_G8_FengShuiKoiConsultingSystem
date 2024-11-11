import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Tag, notification, Input, Space } from 'antd';
import { EyeOutlined, SearchOutlined } from '@ant-design/icons';
import api from '../axious/axious';
import { getToken } from '../service/localStorageService';
import cartLogo from '../assets/images/cart_logo.svg';
import Navbar from '../layouts/header-footer/Navbar';
import Footer from '../layouts/header-footer/Footer';

interface ConsultationResult {
  id: number;
  status: string;
  description: string;
}

const ViewHistory: React.FC = () => {
  const [consultationResults, setConsultationResults] = useState<ConsultationResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedDescription, setSelectedDescription] = useState<string | null>(null);
  const [search, setSearch] = useState('');
  const [searchData, setSearchData] = useState<string>('');
  const [userEmail, setUserEmail] = useState<string>('');

  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const response = await api.get('/users/my-info', {
          headers: { Authorization: `Bearer ${getToken()}` },
        });
        setUserEmail(response.data.result.email || '');
      } catch (error) {
        notification.error({ message: 'Failed to load user information' });
      }
    };
    fetchUserDetails();
  }, []);

  useEffect(() => {
    if (userEmail) {
      fetchConsultationResults();
    }
  }, [userEmail, search]);

  const fetchConsultationResults = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/api/consultation-results/user/${userEmail}`, {
        headers: { Authorization: `Bearer ${getToken()}` },
        params: { search },
      });
      setConsultationResults(response.data.result || []);
    } catch (error) {
      notification.error({ message: 'Failed to load consultation results' });
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (value: string) => {
    setSearch(value);
  };

  const showModal = (description: string) => {
    setSelectedDescription(description);
    setIsModalVisible(true);
  };

  const columns = [
    {
      title: 'STT',
      dataIndex: 'id',
      key: 'id',
      render: (_: any, __: any, index: number) => index + 1,
      // Inline style for column header
      titleStyle: { color: '#888', fontWeight: 500 }
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      sorter: (a: ConsultationResult, b: ConsultationResult) => {
        const statusOrder: { [key: string]: number } = { 'PENDING': 1, 'COMPLETED': 2, 'CANCELLED': 3 };
        return statusOrder[a.status] - statusOrder[b.status];
      },
      render: (status: string) => (
        <Tag style={{ backgroundColor: status === 'COMPLETED' ? 'green' : status === 'PENDING' ? 'orange' : 'red', color: '#fff' }}>
          {status}
        </Tag>
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: any, record: ConsultationResult) => (
        <Button
          icon={<EyeOutlined />}
          onClick={() => showModal(record.description)}
          style={{ borderColor: '#333', color: '#333' }}
        >
          View Description
        </Button>
      ),
    },
  ];

  return (
    <div style={{ padding: '20px', backgroundColor: '#f4f6f9', marginTop: '100px' }}>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div style={{
        maxWidth: '1200px', margin: 'auto', background: '#fff', padding: '20px',
        borderRadius: '8px', boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', marginBottom: '20px' }}>
          <img src={cartLogo} alt="Logo" style={{ width: '50px', marginRight: '10px' }} />
          <h2 style={{ fontSize: '24px', fontWeight: 600, color: '#333' }}>Consultation History</h2>
        </div>
        <Table
          columns={columns}
          dataSource={consultationResults}
          loading={loading}
          rowKey="id"
          pagination={{ pageSize: 5 }}
          style={{ border: '1px solid #ddd', borderRadius: '8px', overflow: 'hidden' }}
        />
        <Modal
          title="Consultation Description"
          visible={isModalVisible}
          onCancel={() => setIsModalVisible(false)}
          footer={null}
          bodyStyle={{ padding: '20px', fontSize: '16px', color: '#555' }}
        >
          <p>{selectedDescription}</p>
        </Modal>
      </div>
      <Footer />
    </div>
  );
};

export default ViewHistory;
