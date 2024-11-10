import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, message } from 'antd';
import api from '../../axious/axious';
import { getToken } from '../../service/localStorageService';
import '../../css/BillPage.css';

// Import các đường dẫn SVG
import billLogo from '../../assets/images/bill_logo.svg';
import cartLogo from '../../assets/images/cart_logo.svg';
import moneyLogo from '../../assets/images/money_logo.svg';
import walletLogo from '../../assets/images/wallet_logo.svg';
import personLogo from '../../assets/images/person_logo.svg';
import statusLogo from '../../assets/images/status-warning_logo.svg';
import dateLogo from '../../assets/images/date_logo.svg';
import vnPayLogo from '../../assets/images/v-vnpay-svgrepo-com.svg';

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

    document.body.classList.add('khoi_body');

  // Gỡ bỏ lớp `khoi_body` khi component bị unmount
  return () => {
    document.body.classList.remove('khoi_body');
  };
  }, [billId]);

  const handlePaymentVNPay = async () => {
    if (bill) {
      try {
        const amount = bill.totalAmount;
        const token = getToken();

        if (!amount || amount <= 0 || !token) {
          message.error('Invalid amount or token!');
          return;
        }

        localStorage.setItem('originalBillId', bill.id.toString());

        const response = await fetch(`http://localhost:9090/vn_pay/create_vn_pay?amount=${encodeURIComponent(amount)}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        const data = await response.json();
        if (data.status === 'OK' && data.url) {
          window.location.href = data.url;
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
    <div className="khoi_bill_page_bill-container">
      <h1>Bill Details</h1>
      
      <div className="khoi_bill_page_bill-info">
          <p>
              <img src={billLogo} alt="Bill ID" className="khoi_bill_page_icon" />
              <strong>Bill ID: </strong> {bill.id}
          </p>
          <p>
              <img src={personLogo} alt="Created By" className="khoi_bill_page_icon" />
              <strong>Created By: </strong> {bill.createdBy}
          </p>
          <p>
              <img src={dateLogo} alt="Creation Date" className="khoi_bill_page_icon" />
              <strong>Creation Date: </strong> {new Date(bill.createdDate).toLocaleString()}
          </p>
          <p>
              <img src={cartLogo} alt="Subtotal" className="khoi_bill_page_icon" />
              <strong>Subtotal: </strong> {bill.subAmount.toLocaleString()} VND
          </p>
          <p>
              <img src={moneyLogo} alt="VAT" className="khoi_bill_page_icon" />
              <strong>VAT: </strong> {bill.vat * 100}%
          </p>
          <p>
              <img src={moneyLogo} alt="VAT Amount" className="khoi_bill_page_icon" />
              <strong>VAT Amount: </strong> {bill.vatAmount.toLocaleString()} VND
          </p>
          <p>
              <img src={statusLogo} alt="Status" className="khoi_bill_page_icon" />
              <strong>Status: </strong> {bill.status}
          </p>

          <div className="khoi_bill_page_divider"></div>

          <p className="khoi_bill_page_total-amount">
              <img src={walletLogo} alt="Total Amount" className="khoi_bill_page_icon" />
              <strong>Total Amount: {bill.totalAmount.toLocaleString()} VND</strong>
          </p>
      </div>
      
      <div className="khoi_bill_page_vnpay-logo-container">
          <img src={vnPayLogo} alt="VNPay Logo" className="khoi_bill_page_vnpay-logo" />
      </div>

      <Button
          type="primary"
          onClick={handlePaymentVNPay}
          className="khoi_bill_page_pay-button"
      >
          Pay with VNPay
      </Button>
  </div>
  );
};

export default BillPage;
