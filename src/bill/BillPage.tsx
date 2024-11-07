import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, message } from 'antd';
import api from '../axious/axious';
import { getToken } from '../service/localStorageService';

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
  const [bill, setBill] = useState<Bill | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchBillDetails = async () => {
      try {
        if (!billId) {
          message.error('ID hóa đơn không hợp lệ!');
          return;
        }

        const response = await api.get(`/api/bills/${billId}`);
        if (response.data.code === 1000) {
          setBill(response.data.result);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        message.error('Không thể tải thông tin hóa đơn!');
        console.error('Error fetching bill details:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchBillDetails();
  }, [billId]);

  const handlePaymentVNPay = async () => {
    if (bill) {
      try {
        const amount = bill.totalAmount;
        const token = getToken();
  
        if (!amount || amount <= 0 || !token) {
          message.error('Số tiền hoặc token không hợp lệ!');
          return;
        }
  
        // Gọi API tạo liên kết thanh toán với `amount`
        const response = await fetch(`http://localhost:9090/vn_pay/create_vn_pay?amount=${encodeURIComponent(amount)}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
  
        const data = await response.json();
        if (data.status === 'OK' && data.url) {
          // Chuyển hướng đến URL được cung cấp bởi VNPay
          window.location.href = data.url;
        } else {
          message.error('Lỗi khi tạo liên kết thanh toán!');
        }
      } catch (error) {
        message.error('Lỗi khi tạo liên kết thanh toán!');
        console.error('Error creating VNPay payment link:', error);
      }
    } else {
      message.error('Hóa đơn không hợp lệ!');
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
    </div>
  );
};

export default BillPage;
