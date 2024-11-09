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
          message.error('Invalid bill ID!');
          return;
        }

        const response = await api.get(`/api/bills/${billId}`);
        if (response.data.code === 1000) {
          setBill(response.data.result);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        message.error('Unable to load bill information!');
        console.error('Error fetching bill details:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchBillDetails();
  }, [billId]);

  const handlePaymentVNPay = async () => {
    console.log("handlePaymentVNPay called");
    if (bill) {
      console.log(bill);

      try {
        const amount = bill.totalAmount;
        const token = getToken();
  
        if (!amount || amount <= 0 || !token) {
          message.error('Invalid amount or token!');
          return;
        }

        // Store the billId before redirecting
        localStorage.setItem('originalBillId', bill.id.toString());

        // Create VNPay payment link
        const response = await fetch(`http://localhost:9090/vn_pay/create_vn_pay?amount=${encodeURIComponent(amount)}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        const data = await response.json();
        if (data.status === 'OK' && data.url) {
          window.location.href = data.url; // Redirect to VNPay URL
        } else {
          message.error('Error creating payment link!');
        }
      } catch (error) {
        message.error('Error creating payment link!');
        console.error('Error creating VNPay payment link:', error);
      }
    } else {
      message.error('Invalid bill!');
    }
  };

  if (loading) {
    return <p>Loading bill information...</p>;
  }

  if (!bill) {
    return <p>Bill not found!</p>;
  }

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Bill Details</h1>
      <p><strong>Bill ID:</strong> {bill.id}</p>
      <p><strong>Created By:</strong> {bill.createdBy}</p>
      <p><strong>Creation Date:</strong> {new Date(bill.createdDate).toLocaleString()}</p>
      <p><strong>Subtotal:</strong> {bill.subAmount.toLocaleString()} VND</p>
<p><strong>VAT:</strong> {bill.vat * 100}%</p>
      <p><strong>VAT Amount:</strong> {bill.vatAmount.toLocaleString()} VND</p>
      <p><strong>Total Amount:</strong> {bill.totalAmount.toLocaleString()} VND</p>
      <p><strong>Status:</strong> {bill.status}</p>

      <Button
        type="primary"
        onClick={handlePaymentVNPay}
        style={{ marginTop: '20px' }}
      >
        Pay with VNPay
      </Button>
    </div>
  );
};

export default BillPage;