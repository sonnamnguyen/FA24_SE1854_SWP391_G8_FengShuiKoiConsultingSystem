import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../../axious/axious';
import { Form, Input, Radio, Button, message, Select } from 'antd';
import { getToken } from '../../../service/localStorageService';
import '../../../css/ConsultationRequest.css';

const { Option } = Select;

const isVietnamesePhoneNumber = (number: string): boolean => {
  return /^(0|\+84|\+840)(3|5|7|8|9)[0-9]{8}$/.test(number);
};

const isValidEmail = (email: string): boolean => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

type PackageInfo = {
  name: string;
  description: string;
  price: number;
};

const ConsultationRequest: React.FC = () => {
  const navigate = useNavigate();
  const [packageId, setPackageId] = useState<number>(1);
  const [yob, setYob] = useState<number | null>(new Date().getFullYear() - 20);
  const [description, setDescription] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [years, setYears] = useState<number[]>([]);
  const [fullName, setFullName] = useState<string>('');
  const [gender, setGender] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [phone, setPhone] = useState<string>('');
  const [selectedPackageInfo, setSelectedPackageInfo] = useState<PackageInfo | null>(null);

  useEffect(() => {
    const token = getToken();
    if (!token) {
      message.warning('Please log in to continue.');
      navigate('/');
      return;
    }

    const currentYear = new Date().getFullYear();
    const yearsArray = Array.from({ length: currentYear - 1900 + 1 }, (_, index) => 1900 + index);
    setYears(yearsArray);

    const fetchAccountInfo = async () => {
      try {
        const response = await api.get('/users/my-info', {
          headers: { Authorization: `Bearer ${token}` },
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
      }
    };

    fetchAccountInfo();
    fetchPackageInfo(1);

    document.body.classList.add('khoi_body');

  // Gỡ bỏ lớp `khoi_body` khi component bị unmount
  return () => {
    document.body.classList.remove('khoi_body');
  };
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
    }
  };

  const handlePackageChange = (id: number) => {
    setPackageId(id);
    fetchPackageInfo(id);
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      if (!fullName) {
        message.error('Full name is required.');
        setLoading(false);
        return;
      }
      if (fullName.length > 100) {
        message.error('Full name cannot exceed 100 characters.');
        setLoading(false);
        return;
      }
      if (!gender) {
        message.error('Gender is required.');
        setLoading(false);
        return;
      }
      if (!email || !isValidEmail(email)) {
        message.error('Please enter a valid email.');
        setLoading(false);
        return;
      }
      if (!phone || !isVietnamesePhoneNumber(phone)) {
        message.error('Please enter a valid Vietnamese phone number.');
        setLoading(false);
        return;
      }
      const currentYear = new Date().getFullYear();
      if (!yob || yob < 1900 || yob > currentYear) {
        message.error('Year of birth must be between 1900 and the current year.');
        setLoading(false);
        return;
      }
      if (!description || description.length < 10 || description.length > 1000) {
        message.error('The request description must be between 10 and 1000 characters.');
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
        localStorage.setItem('requestId', requestId);
        localStorage.setItem('selectedPackageId', packageId.toString());
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
    <div className="khoi_con_req_consultation-form-container">
      <h1>Consultation Request</h1>
      <Form onFinish={handleSubmit} layout="vertical">
        <Form.Item label="Full Name" required className="khoi_con_req_form-item">
          <Input
            placeholder="Full Name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            className={!fullName ? 'khoi_con_req_input-error' : ''}
          />
        </Form.Item>

        <Form.Item label="Gender" required className="khoi_con_req_form-item">
          <Radio.Group
            onChange={(e) => setGender(e.target.value)}
            value={gender}
            className={!gender ? 'khoi_con_req_input-error' : ''}
          >
            <Radio value="MALE">Male</Radio>
            <Radio value="FEMALE">Female</Radio>
            <Radio value="OTHER">Other</Radio>
          </Radio.Group>
        </Form.Item>

        <Form.Item label="Email" required className="khoi_con_req_form-item">
          <Input
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className={!email ? 'khoi_con_req_input-error' : ''}
          />
        </Form.Item>

        <Form.Item label="Phone Number" required className="khoi_con_req_form-item">
          <Input
            placeholder="Phone Number"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            className={!phone ? 'khoi_con_req_input-error' : ''}
          />
        </Form.Item>

        <Form.Item label="Select Year of Birth" required className="khoi_con_req_form-item">
          <Select
            showSearch
            placeholder="Select or enter year of birth"
            value={yob || undefined}
            onChange={(value) => setYob(value)}
            filterOption={(input, option) => {
              const value = option?.value?.toString().toLowerCase();
              return value ? value.includes(input.toLowerCase()) : false;
            }}
            className={!yob ? 'khoi_con_req_input-error' : ''}
          >
            {years.map((year) => (
              <Option key={year} value={year}>
                {year}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label="Request Description" required className="khoi_con_req_form-item">
          <Input.TextArea
            rows={4}
            placeholder="Enter request description (between 10 and 1000 characters)"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className={!description ? 'khoi_con_req_input-error' : ''}
          />
        </Form.Item>

        <Form.Item label="Select Package" required className="khoi_con_req_form-item">
          <Radio.Group
            onChange={(e) => handlePackageChange(e.target.value)}
            value={packageId}
            className={!packageId ? 'khoi_con_req_input-error' : ''}
          >
            <Radio value={1}>Fish Consultation Package</Radio>
            <Radio value={2}>Pond Consultation Package</Radio>
            <Radio value={3}>Fish and Pond Consultation Package</Radio>
          </Radio.Group>
        </Form.Item>

        <Form.Item label="Package Information" className="khoi_con_req_form-item">
          {selectedPackageInfo && (
            <div className="khoi_con_req_package-info">
              <div className="khoi_con_req_package-info-content">
                <h3>{selectedPackageInfo.name}</h3>
                <p>{selectedPackageInfo.description}</p>
                
                {packageId === 3 ? (
                  <div className="khoi_con_req_price">
                    <span className="khoi_con_req_original-price">500,000 VND</span>
                    <span className="khoi_con_req_discounted-price">450,000 VND</span>
                    <span className="khoi_con_req_discount-badge">Sale 10%</span>
                  </div>
                ) : (
                  <p className="khoi_con_req_discounted-price">Price: {selectedPackageInfo.price.toLocaleString()} VND</p>
                )}
              </div>
            </div>
          )}
        </Form.Item>

        <Form.Item className="khoi_con_req_form-item">
          <Button type="primary" htmlType="submit" loading={loading} className="khoi_con_req_submit-button">
            Continue
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default ConsultationRequest;
