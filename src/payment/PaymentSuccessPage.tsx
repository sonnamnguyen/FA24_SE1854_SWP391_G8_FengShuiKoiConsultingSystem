import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { message, Modal, Spin } from 'antd';
import api from '../axious/axious';
import { getToken } from '../service/localStorageService';

const PaymentSuccessPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const handleVNPayResponse = async () => {
      const queryParams = new URLSearchParams(location.search);

      // Retrieve billId from localStorage or from query parameters
      const originalBillId = localStorage.getItem('originalBillId');
      const returnedBillId = queryParams.get('billId') || queryParams.get('vnp_TxnRef');
      const billId = originalBillId || returnedBillId;

      if (!billId) {
        message.error('Thiếu thông tin hóa đơn!');
        navigate('/error');
        return;
      }

      const responseCode = '00'; // Indicates successful payment
      const amount = queryParams.get('vnp_Amount') || '100000'; // Example default amount
      const bankCode = queryParams.get('vnp_BankCode') || 'NCB'; // Example default bank code
      const orderInfo = queryParams.get('vnp_OrderInfo') || 'Thanh toán đơn hàng';

      try {
        const token = getToken();
        if (!token) {
          message.error('Không thể xác thực người dùng!');
          navigate('/error');
          return;
        }

        // Simulate processing time (3-5 seconds)
        await new Promise(resolve => setTimeout(resolve, Math.floor(Math.random() * 2000) + 3000));

        // Make the request to update the payment status
        const response = await api.get(`/vn_pay/vnpay_infor?vnp_ResponseCode=${responseCode}&billId=${billId}&vnp_Amount=${amount}&vnp_BankCode=${bankCode}&vnp_OrderInfo=${encodeURIComponent(orderInfo)}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200) {
          message.success(response.data || 'Thanh toán thành công!');

          // Assuming `requestId` is returned in the response, you can extract and store it
          const requestId = response.data.result?.requestId || 'defaultRequestId'; // Replace with actual response handling logic
          
          // Store requestId and billId in localStorage
          localStorage.setItem('originalBillId', billId);
          

          // Navigate to ConsultationRequestDetail page
          navigate('/consultation-request-detail', {
            state: {
              requestId,
              billId,
            },
          });
        } else {
          message.error(response.data || 'Thanh toán không thành công!');
          navigate('/error');
        }
      } catch (error) {
        message.error('Lỗi trong quá trình xử lý kết quả thanh toán!');
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
      bodyStyle={{ textAlign: 'center' }} // Correct usage for styling
    >
      <Spin size="large" />
      <p>Đang xử lý kết quả thanh toán...</p>
    </Modal>
  );
};

export default PaymentSuccessPage;