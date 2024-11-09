import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Radio, Button, message, Modal } from 'antd';
import api from '../axious/axious';

const PaymentPage: React.FC = () => {
  const { requestId } = useParams<{ requestId: string }>(); // Get requestId from URL
  const navigate = useNavigate();
  const [paymentId, setPaymentId] = useState<number>(1); // Default is VNPay
  const [loading, setLoading] = useState<boolean>(false);
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false); // Modal for PayPal

  const handlePayment = async () => {
    setLoading(true);
    try {
      const billResponse = await api.post(`/api/bills/request/${requestId}/payments/${paymentId}`, {
        description: 'Payment for consultation package',
      });

      const billId = billResponse.data.result.id; // Get billId from API

      // Navigate to BillPage with billId after creating the bill
      navigate(`/bill/${billId}`);
    } catch (error) {
      message.error('Error creating bill!');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handlePayPal = () => {
    setIsModalVisible(true); // Open modal for PayPal
  };

  const closeModal = () => {
    setIsModalVisible(false); // Close modal
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Select Payment Method</h1>
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
          Pay
        </Button>
        <Button
          style={{ marginLeft: '10px' }}
          onClick={() => navigate('/')}
        >
          Back to Home
        </Button>
      </div>

      <Modal
        title="Notification"
        visible={isModalVisible}
        onOk={closeModal}
        onCancel={closeModal}
        okText="Agree"
        cancelText="Cancel"
      >
        <p>PayPal payment functionality is being updated. Please select another payment method.</p>
      </Modal>
    </div>
  );
};

export default PaymentPage;