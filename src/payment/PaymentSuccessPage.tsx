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

      // Lấy billId từ URL
      const billId = queryParams.get('billId') || queryParams.get('vnp_TxnRef');

      // Cố định giá trị cho các tham số
      const responseCode = '00'; // Luôn success
      const amount = queryParams.get('vnp_Amount') || '100000'; // Giá trị mặc định
      const bankCode = queryParams.get('vnp_BankCode') || 'NCB'; // Giá trị mặc định
      const orderInfo = queryParams.get('vnp_OrderInfo') || 'Thanh toán đơn hàng';

      if (!billId) {
        message.error('Thiếu thông tin hóa đơn!');
        navigate('/error'); // Điều hướng đến trang lỗi nếu thiếu billId
        return;
      }

      try {
        // Lấy token nếu có
        const token = getToken();
        if (!token) {
          message.error('Không thể xác thực người dùng!');
          navigate('/error'); // Điều hướng đến trang lỗi nếu không có token
          return;
        }

        // Gọi API để xử lý kết quả thanh toán với các tham số cố định
        const response = await api.get(`/vn_pay/vnpay_infor?vnp_ResponseCode=${responseCode}&billId=${billId}&vnp_Amount=${amount}&vnp_BankCode=${bankCode}&vnp_OrderInfo=${encodeURIComponent(orderInfo)}`, {
          headers: {
            Authorization: `Bearer ${token}` // Thêm token vào header
          }
        });

        if (response.status === 200) {
          message.success(response.data || 'Thanh toán thành công!');
        } else {
          message.error(response.data || 'Thanh toán không thành công!');
          navigate('/error'); // Điều hướng đến trang lỗi khi thanh toán không thành công
        }
      } catch (error) {
        message.error('Lỗi trong quá trình xử lý kết quả thanh toán!');
        console.error('Error handling VNPay response:', error);
        navigate('/error'); // Điều hướng đến trang lỗi khi có lỗi xảy ra
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
