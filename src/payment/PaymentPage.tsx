import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Radio, Button, message, Modal } from 'antd';
import api from '../axious/axious';

const PaymentPage: React.FC = () => {
  const { requestId } = useParams<{ requestId: string }>(); // Lấy requestId từ URL
  const navigate = useNavigate();
  const [paymentId, setPaymentId] = useState<number>(1); // Mặc định là VNPay
  const [loading, setLoading] = useState<boolean>(false);
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false); // Modal cho PayPal

  const handlePayment = async () => {
    setLoading(true);
    try {
      const billResponse = await api.post(`/api/bills/request/${requestId}/payments/${paymentId}`, {
        description: 'Thanh toán cho gói tư vấn',
      });

      const billId = billResponse.data.result.id; // Lấy billId từ API

      // Điều hướng sang BillPage với billId sau khi tạo xong hóa đơn
      navigate(`/bill/${billId}`);
    } catch (error) {
      message.error('Lỗi khi tạo hóa đơn!');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handlePayPal = () => {
    setIsModalVisible(true); // Mở modal cho PayPal
  };

  const closeModal = () => {
    setIsModalVisible(false); // Đóng modal
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Chọn phương thức thanh toán</h1>
      <Radio.Group
        onChange={(e) => setPaymentId(e.target.value)}
        value={paymentId}
      >
        <Radio value={1}>VNPay</Radio>
        <Radio value={2}>PayPal</Radio>
      </Radio.Group>
      <div style={{ marginTop: '20px' }}>
        <Button
          type="primary"
          onClick={paymentId === 1 ? handlePayment : handlePayPal}
          loading={loading}
        >
          Thanh Toán
        </Button>
        <Button
          style={{ marginLeft: '10px' }}
          onClick={() => navigate('/')}
        >
          Quay về Trang Chủ
        </Button>
      </div>

      <Modal
        title="Thông báo"
        visible={isModalVisible}
        onOk={closeModal}
        onCancel={closeModal}
        okText="Đồng ý"
        cancelText="Hủy"
      >
        <p>Chức năng thanh toán bằng PayPal đang được cập nhật. Vui lòng chọn phương thức thanh toán khác.</p>
      </Modal>
    </div>
  );
};

export default PaymentPage;