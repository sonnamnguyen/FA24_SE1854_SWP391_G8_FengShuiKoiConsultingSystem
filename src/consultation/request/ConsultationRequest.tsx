import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../axious/axious'; 
import { Form, Input, Radio, Button, message, Select } from 'antd';
import '../../css/ConsultationRequest.css'; 

const { Option } = Select;

// Utility function for validating Vietnamese phone numbers
const isVietnamesePhoneNumber = (number: string): boolean => {
  return /(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b/.test(number);
};

const ConsultationRequest: React.FC = () => {
  const navigate = useNavigate();
  const [packageId, setPackageId] = useState<number>(1); // Default package is 1
  const [yob, setYob] = useState<number | null>(new Date().getFullYear() - 20); // Default birth year is 20 years ago
  const [description, setDescription] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [years, setYears] = useState<number[]>([]);
  const [fullName, setFullName] = useState<string>('');
  const [gender, setGender] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [phone, setPhone] = useState<string>('');
  const [selectedPackageInfo, setSelectedPackageInfo] = useState<{ name: string, description: string, price: number } | null>(null);

  useEffect(() => {
    const currentYear = new Date().getFullYear();
    const yearsArray = Array.from({ length: currentYear - 1900 + 1 }, (_, index) => 1900 + index);
    setYears(yearsArray);

    const fetchAccountInfo = async () => {
      try {
        const response = await api.get('/users/my-info', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });

        if (response.data && response.data.result) {
          const userData = response.data.result;
          setFullName(userData.fullName || '');
          setGender(userData.gender || '');
          setEmail(userData.email || '');
          setPhone(userData.phoneNumber || '');
        } else {
          throw new Error('Invalid user data response');
        }
      } catch (error) {
        message.error('Không thể lấy thông tin tài khoản.');
        console.error('Error fetching user info:', error);
      }
    };

    fetchAccountInfo();

    // Fetch package info for initial selection
    fetchPackageInfo(1); // Default package information is shown for package 1
  }, []);

  const fetchPackageInfo = async (id: number) => {
    try {
      const response = await api.get(`/packages/${id}`);
      if (response.data && response.data.result) {
        const packageData = response.data.result;
        setSelectedPackageInfo({
          name: packageData.package_name,
          description: packageData.description,
          price: packageData.price,
        });
      } else {
        throw new Error(`Invalid package data for id ${id}`);
      }
    } catch (error) {
      message.error('Không thể lấy thông tin gói dịch vụ.');
      console.error(`Error fetching package data for id ${id}:`, error);
    }
  };

  const handlePackageChange = (id: number) => {
    setPackageId(id);
    fetchPackageInfo(id);
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        message.error('Vui lòng nhập email hợp lệ.');
        setLoading(false);
        return;
      }
      if (!isVietnamesePhoneNumber(phone)) {
        message.error('Vui lòng nhập số điện thoại Việt Nam hợp lệ.');
        setLoading(false);
        return;
      }
      if (description.length < 10) {
        message.error('Mô tả yêu cầu phải có ít nhất 10 ký tự.');
        setLoading(false);
        return;
      }
  
      const response = await api.post('/api/consultation-requests', {
        packageId,
        yob,
        description,
        fullName,
        gender,
        email,
        phone,
      });
  
      if (response.data.code === 1000) {
        const requestId = response.data.result.id;
        // Save requestId and packageId to localStorage
        localStorage.setItem('requestId', requestId);
        localStorage.setItem('selectedPackageId', packageId.toString()); // Convert packageId to string for storage
        message.success('Tạo yêu cầu thành công!');
        navigate(`/consultation-request/${requestId}/payment`);
      } else {
        throw new Error(response.data.message);
      }
    } catch (error) {
      message.error('Có lỗi xảy ra, vui lòng thử lại!');
    } finally {
      setLoading(false);
    }
  };
  
  

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Yêu cầu tư vấn</h1>
      <Form onFinish={handleSubmit} layout="vertical">
        <Form.Item label="Họ và tên" required>
          <Input
            placeholder="Họ và tên"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
          />
        </Form.Item>

        <Form.Item label="Giới tính" required>
          <Radio.Group
            onChange={(e) => setGender(e.target.value)}
            value={gender}
          >
            <Radio value="MALE">Nam</Radio>
            <Radio value="FEMALE">Nữ</Radio>
            <Radio value="OTHER">Khác</Radio>
          </Radio.Group>
        </Form.Item>

        <Form.Item label="Email" required>
          <Input
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </Form.Item>

        <Form.Item label="Số điện thoại" required>
          <Input
            placeholder="Số điện thoại"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
          />
        </Form.Item>

        <Form.Item label="Chọn năm sinh" required>
          <Select
            showSearch
            placeholder="Chọn hoặc nhập năm sinh"
            value={yob || undefined}
            onChange={(value) => setYob(value)}
            filterOption={(input, option) => {
              const value = option?.value?.toString().toLowerCase();
              return value ? value.includes(input.toLowerCase()) : false;
            }}
          >
            {years.map((year) => (
              <Option key={year} value={year}>
                {year}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label="Mô tả yêu cầu" required>
          <Input.TextArea
            rows={4}
            placeholder="Nhập mô tả yêu cầu (ít nhất 10 ký tự)"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Form.Item>
        
        <Form.Item label="Chọn gói dịch vụ" required>
          <Radio.Group
            onChange={(e) => handlePackageChange(e.target.value)}
            value={packageId}
          >
            <Radio value={1}>Gói tư vấn cá</Radio>
            <Radio value={2}>Gói tư vấn hồ</Radio>
            <Radio value={3}>Gói tư vấn cá và hồ</Radio>
          </Radio.Group>
        </Form.Item>

        <Form.Item label="Thông tin gói dịch vụ">
          {selectedPackageInfo && (
            <div>
              <h3>{selectedPackageInfo.name}</h3>
              <p>{selectedPackageInfo.description}</p>
              <p>Giá: {selectedPackageInfo.price.toLocaleString()} VND</p>
            </div>
          )}
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>
            Thanh Toán
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default ConsultationRequest;
