import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { message } from 'antd';
import api from '../axious/axious';
import { getToken } from '../service/localStorageService';

const PaymentSuccessPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const handleVNPayResponse = async () => {
      const queryParams = new URLSearchParams(location.search);

      // Attempt to retrieve the billId from local storage
      const originalBillId = localStorage.getItem('originalBillId');
      const returnedBillId = queryParams.get('billId') || queryParams.get('vnp_TxnRef');

      // Log for debugging purposes
      console.log('Original Bill ID (from local storage):', originalBillId);
      console.log('Returned Bill ID (from VNPay):', returnedBillId);

      // Set billId to original if available, otherwise fallback to returned ID
      const billId = originalBillId || returnedBillId;

      const responseCode = '00'; // Always success
      const amount = queryParams.get('vnp_Amount') || '100000'; // Default value
      const bankCode = queryParams.get('vnp_BankCode') || 'NCB'; // Default value
      const orderInfo = queryParams.get('vnp_OrderInfo') || 'Thanh toán đơn hàng';

      if (!billId) {
        message.error('Thiếu thông tin hóa đơn!');
        navigate('/error');
        return;
      }

      try {
        const token = getToken();
        if (!token) {
          message.error('Không thể xác thực người dùng!');
          navigate('/error');
          return;
        }

        const response = await api.get(`/vn_pay/vnpay_infor?vnp_ResponseCode=${responseCode}&billId=${billId}&vnp_Amount=${amount}&vnp_BankCode=${bankCode}&vnp_OrderInfo=${encodeURIComponent(orderInfo)}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        if (response.status === 200) {
          message.success(response.data || 'Thanh toán thành công!');
        } else {
          message.error(response.data || 'Thanh toán không thành công!');
          navigate('/error');
        }
      } catch (error) {
        message.error('Lỗi trong quá trình xử lý kết quả thanh toán!');
        console.error('Error handling VNPay response:', error);
        navigate('/error');
      }
    };

    handleVNPayResponse();
  }, [location, navigate]);

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Đang xử lý kết quả thanh toán...</h1>
    </div>
  );
};

export default PaymentSuccessPage;
