import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Button, message, Modal } from 'antd';
import api from '../axious/axious';
import '../css/PaymentPage.css';

import vnpayLogo from '../assets/images/v-vnpay-svgrepo-com.svg';
import paypalLogo from '../assets/images/paypal-svgrepo-com.svg';
import visaLogo from '../assets/images/visa_logo.svg';
import mastercardLogo from '../assets/images/mastercard_logo.svg';
import amexLogo from '../assets/images/amex_logo.svg';
import discoverLogo from '../assets/images/discover_logo.svg';
import unionpayLogo from '../assets/images/unionpay_logo.svg';

import vietcombankLogo from '../assets/images/vietcombank_logo.svg';
import tpbankLogo from '../assets/images/tpbank_logo.svg';
import techcombankLogo from '../assets/images/techcombank_logo.svg';

const PaymentPage: React.FC = () => {
  const { requestId } = useParams<{ requestId: string }>(); 
  const navigate = useNavigate();
  const [paymentId, setPaymentId] = useState<number>(1); 
  const [loading, setLoading] = useState<boolean>(false);
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false); 

  const handlePayment = async () => {
    setLoading(true);
    try {
      const billResponse = await api.post(`/api/bills/request/${requestId}/payments/${paymentId}`, {
        description: 'Payment for consultation package',
      });

      const billId = billResponse.data.result.id; 
      navigate(`/bill/${billId}`); 
    } catch (error) {
      message.error('Error creating bill!');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handlePayPal = () => {
    setIsModalVisible(true); 
  };

  const closeModal = () => {
    setIsModalVisible(false); 
  };

  return (
    <div className="payment-container">
      <h1>Select Payment Method</h1>
      <div className="payment-options">
        <div
          className={`payment-option ${paymentId === 1 ? 'selected' : ''}`}
          onClick={() => setPaymentId(1)}
        >
          <img src={vnpayLogo} alt="VNPay Logo" className="payment-logo-large" />
          <span>VNPay</span>
          <p>VAT 10%</p>
          <div className="bank-logos">
            <img src={vietcombankLogo} alt="Vietcombank" />
            <img src={tpbankLogo} alt="TPBank" />
            <img src={techcombankLogo} alt="Techcombank" />
            <span>+ nhiều ngân hàng khác</span>
          </div>
        </div>
        <div
          className={`payment-option ${paymentId === 2 ? 'selected' : ''}`}
          onClick={() => setPaymentId(2)}
        >
          <img src={paypalLogo} alt="PayPal Logo" className="payment-logo-large" />
          <span>PayPal</span>
          <p>+ $2 / ~ 50,000₫ handling fee</p>
          <div className="accepted-cards">
            <img src={visaLogo} alt="Visa" />
            <img src={mastercardLogo} alt="MasterCard" />
            <img src={amexLogo} alt="AmEx" />
            <img src={discoverLogo} alt="Discover" />
            <img src={unionpayLogo} alt="UnionPay" />
          </div>
        </div>
      </div>
      <div className="payment-buttons">
        <Button
          type="primary"
          onClick={paymentId === 1 ? handlePayment : handlePayPal}
          loading={loading}
        >
          Pay
        </Button>
        <Button onClick={() => navigate('/')}>
          Back to Home
        </Button>
      </div>

      <Modal
        title="Notification"
        visible={isModalVisible}
        onOk={closeModal}
        footer={[
          <Button key="ok" type="primary" onClick={closeModal}>
            Agree
          </Button>,
        ]}
      >
        <p>PayPal payment functionality is being updated. Please select another payment method.</p>
      </Modal>
    </div>
  );
};

export default PaymentPage;
