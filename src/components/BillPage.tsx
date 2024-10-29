import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Button, message } from 'antd';
import api from '../axious/axious'; // Đã config sẵn với token
import { getToken } from '../service/localStorageService'; // Import getToken

interface Bill {
  id: number;
  createdBy: string;
  createdDate: string;
  subAmount: number;
  vat: number;
  vatAmount: number;
  totalAmount: number;
  status: string;
}

const BillPage: React.FC = () => {
  const { billId } = useParams<{ billId: string }>();
  const navigate = useNavigate();
  const [bill, setBill] = useState<Bill | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchBillDetails = async () => {
      try {
        const response = await api.get(`/api/bills/${billId}`);
        if (response.data.code === 1000) {
          setBill(response.data.result);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        message.error('Không thể tải thông tin hóa đơn!');
      } finally {
        setLoading(false);
      }
    };

    fetchBillDetails();
  }, [billId]);

  const handlePaymentVNPay = async () => {
    if (bill) {
      try {
        const token = getToken() ?? ""; // Đảm bảo token không phải null
        const amount = bill.totalAmount;
  
        // Kiểm tra xem amount và token có hợp lệ không
        if (!amount || amount <= 0) {
          message.error("Số tiền không hợp lệ!");
          return;
        }
  
        const response = await api.get(
          `/vn_pay/create_vn_pay?amount=${encodeURIComponent(amount)}&token=${encodeURIComponent(token)}`
        );
  
        if (response.data.status === "OK") {
          const paymentUrl = response.data.result; // Lấy URL thanh toán từ backend
          window.location.href = paymentUrl; // Điều hướng đến URL thanh toán
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        message.error("Lỗi khi tạo liên kết thanh toán!");
        console.error(error);
      }
    }
  };
  
  
  
  

  if (loading) {
    return <p>Đang tải thông tin hóa đơn...</p>;
  }

  if (!bill) {
    return <p>Không tìm thấy hóa đơn!</p>;
  }

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Chi tiết Hóa Đơn</h1>
      <p><strong>Mã hóa đơn:</strong> {bill.id}</p>
      <p><strong>Người tạo:</strong> {bill.createdBy}</p>
      <p><strong>Ngày tạo:</strong> {new Date(bill.createdDate).toLocaleString()}</p>
      <p><strong>Tạm tính:</strong> {bill.subAmount.toLocaleString()} VND</p>
      <p><strong>VAT:</strong> {bill.vat * 100}%</p>
      <p><strong>Tiền VAT:</strong> {bill.vatAmount.toLocaleString()} VND</p>
      <p><strong>Tổng tiền:</strong> {bill.totalAmount.toLocaleString()} VND</p>
      <p><strong>Trạng thái:</strong> {bill.status}</p>

      <Button
        type="primary"
        onClick={handlePaymentVNPay}
        style={{ marginTop: '20px' }}
      >
        Thanh Toán VNPay
      </Button>

      <Button
        style={{ marginTop: '10px' }}
        onClick={() => navigate('/')}
      >
        Quay về Trang Chủ
      </Button>
    </div>
  );
};

export default BillPage;
