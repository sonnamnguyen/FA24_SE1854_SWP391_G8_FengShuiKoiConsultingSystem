import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; // Sử dụng useNavigate
import "../css/VNPaySuccess.css"; // Nhập tệp CSS
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";

const VNPaySuccess: React.FC = () => {
  const navigate = useNavigate(); // Khai báo useNavigate
  const [searchData, setSearchData] = useState<string>("");
  const handleContinueShopping = () => {
    navigate("/"); // Chuyển đến trang chính hoặc trang sản phẩm
  };

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="vnpay-container">
        <h1 className="vnpay-title">Thanh Toán Thành Công!</h1>
        <p className="vnpay-message">
          Cảm ơn bạn đã thanh toán. Đơn hàng của bạn đang được xử lý.
        </p>
        <button className="vnpay-button" onClick={handleContinueShopping}>
          Trở về trang chính
        </button>
      </div>
      <Footer />
    </div>
  );
};

export default VNPaySuccess;
