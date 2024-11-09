import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../axious/axious';
import { Form, Input, Radio, Button, message, Select } from 'antd';
import { getToken } from '../../service/localStorageService';

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
    // Check token before calling API
    const token = getToken();
    if (!token) {
      message.warning('Please log in to continue.');
      navigate('/'); // Redirect to login page (if there is a login page)
      return; // Prevent further actions if there is no token
    }
  
    const currentYear = new Date().getFullYear();
    const yearsArray = Array.from({ length: currentYear - 1900 + 1 }, (_, index) => 1900 + index);
    setYears(yearsArray);
  
    const fetchAccountInfo = async () => {
      try {
        const response = await api.get('/users/my-info', {
          headers: {
            Authorization: `Bearer ${token}`,
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
        message.error('Unable to fetch account information.');
        console.error('Error fetching user info:', error);
      }
    };
  
    fetchAccountInfo();
  
    // Fetch package info for initial selection
    fetchPackageInfo(1); // Default package information is shown for package 1
  }, [navigate]);
  

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
      message.error('Unable to fetch package information.');
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
        message.error('Please enter a valid email.');
        setLoading(false);
        return;
      }
      if (!isVietnamesePhoneNumber(phone)) {
        message.error('Please enter a valid Vietnamese phone number.');
        setLoading(false);
        return;
      }
      if (description.length < 10) {
        message.error('The request description must be at least 10 characters.');
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
        message.success('Request created successfully!');
        navigate(`/consultation-request/${requestId}/payment`);
      } else {
        throw new Error(response.data.message);
      }
    } catch (error) {
      message.error('An error occurred, please try again!');
    } finally {
      setLoading(false);
    }
  };
  
  

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Consultation Request</h1>
      <Form onFinish={handleSubmit} layout="vertical">
        <Form.Item label="Full Name" required>
          <Input
            placeholder="Full Name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
          />
        </Form.Item>

        <Form.Item label="Gender" required>
          <Radio.Group
            onChange={(e) => setGender(e.target.value)}
            value={gender}
          >
            <Radio value="MALE">Male</Radio>
            <Radio value="FEMALE">Female</Radio>
            <Radio value="OTHER">Other</Radio>
          </Radio.Group>
        </Form.Item>

        <Form.Item label="Email" required>
          <Input
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </Form.Item>

        <Form.Item label="Phone Number" required>
          <Input
            placeholder="Phone Number"
            value={phone}
onChange={(e) => setPhone(e.target.value)}
          />
        </Form.Item>

        <Form.Item label="Select Year of Birth" required>
          <Select
            showSearch
            placeholder="Select or enter year of birth"
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

        <Form.Item label="Request Description" required>
          <Input.TextArea
            rows={4}
            placeholder="Enter request description (at least 10 characters)"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Form.Item>
        
        <Form.Item label="Select Package" required>
          <Radio.Group
            onChange={(e) => handlePackageChange(e.target.value)}
            value={packageId}
          >
            <Radio value={1}>Fish Consultation Package</Radio>
            <Radio value={2}>Pond Consultation Package</Radio>
            <Radio value={3}>Fish and Pond Consultation Package</Radio>
          </Radio.Group>
        </Form.Item>

        <Form.Item label="Package Information">
          {selectedPackageInfo && (
            <div>
              <h3>{selectedPackageInfo.name}</h3>
              <p>{selectedPackageInfo.description}</p>
              <p>Price: {selectedPackageInfo.price.toLocaleString()} VND</p>
            </div>
          )}
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>
            Pay
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default ConsultationRequest;