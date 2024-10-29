import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../axious/axious'; // API đã config
import { Form, Input, Radio, Button, message, Select } from 'antd';

const { Option } = Select;

const ConsultationRequest: React.FC = () => {
  const navigate = useNavigate();
  const [packageId, setPackageId] = useState<number>(1); // Mặc định là gói 1
  const [yob, setYob] = useState<number | null>(null);
  const [description, setDescription] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [years, setYears] = useState<number[]>([]); // Danh sách năm sinh

  // Khởi tạo danh sách năm từ 1900 đến năm hiện tại
  useEffect(() => {
    const currentYear = new Date().getFullYear();
    const yearsArray = Array.from({ length: currentYear - 1900 + 1 }, (_, index) => 1900 + index);
    setYears(yearsArray);
  }, []);

  const handleSubmit = async () => {
    setLoading(true);
    try {
      const response = await api.post('/api/consultation-requests', {
        packageId,
        yob,
        description,
      });

      if (response.data.code === 1000) {
        message.success('Tạo yêu cầu thành công!');
        const requestId = response.data.result.id;

        // Điều hướng sang trang thanh toán
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
            placeholder="Nhập mô tả yêu cầu"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </Form.Item>

        <Form.Item label="Chọn gói dịch vụ" required>
          <Radio.Group
            onChange={(e) => setPackageId(e.target.value)}
            value={packageId}
          >
            <Radio value={1}>Gói tư vấn cá</Radio>
            <Radio value={2}>Gói tư vấn hồ</Radio>
            <Radio value={3}>Gói tư vấn cá và hồ</Radio>
          </Radio.Group>
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
