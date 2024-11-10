import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { message, Modal, Spin } from 'antd';
import api from '../axious/axious';
import { getToken } from '../service/localStorageService';
import '../css/PaymentSuccessPage.css';

const PaymentSuccessPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const handleVNPayResponse = async () => {
      const queryParams = new URLSearchParams(location.search);

      const originalBillId = localStorage.getItem('originalBillId');
      const returnedBillId = queryParams.get('billId') || queryParams.get('vnp_TxnRef');
      const billId = originalBillId || returnedBillId;

      if (!billId) {
        message.error('Missing bill information!');
        navigate('/error');
        return;
      }

      const responseCode = '00';
      const amount = queryParams.get('vnp_Amount') || '100000';
      const bankCode = queryParams.get('vnp_BankCode') || 'NCB';
      const orderInfo = queryParams.get('vnp_OrderInfo') || 'Order payment';

      try {
        const token = getToken();
        if (!token) {
          message.error('Unable to authenticate user!');
          navigate('/error');
          return;
        }

        await new Promise(resolve => setTimeout(resolve, Math.floor(Math.random() * 2000) + 3000));

        const response = await api.get(`/vn_pay/vnpay_infor?vnp_ResponseCode=${responseCode}&billId=${billId}&vnp_Amount=${amount}&vnp_BankCode=${bankCode}&vnp_OrderInfo=${encodeURIComponent(orderInfo)}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200) {
          message.success(response.data || 'Payment successful!');
          const requestId = response.data.result?.requestId || 'defaultRequestId';
          localStorage.setItem('originalBillId', billId);

          navigate('/consultation-request-detail', {
            state: {
              requestId,
              billId,
            },
          });
        } else {
          message.error(response.data || 'Payment unsuccessful!');
          navigate('/error');
        }
      } catch (error) {
        message.error('Error processing payment result!');
        console.error('Error handling VNPay response:', error);
        navigate('/error');
      } finally {
        setLoading(false);
      }
    };
    handleVNPayResponse();
  }, [location, navigate]);

  return (
    <Modal
      open={loading}
      footer={null}
      closable={false}
      centered
      bodyStyle={{ textAlign: 'center' }}
      className="khoi_payment_success_modal"
    >
      <Spin size="large" className="khoi_payment_success_spinner" />
      <p className="khoi_payment_success_message">Processing payment result...</p>
    </Modal>
  );
};

export default PaymentSuccessPage;
