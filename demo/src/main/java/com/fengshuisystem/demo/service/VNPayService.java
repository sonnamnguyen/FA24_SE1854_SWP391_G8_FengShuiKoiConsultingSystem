package com.fengshuisystem.demo.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class VNPayService {

    // Hàm tạo URL thanh toán qua VNPay
    public String createPaymentURL(Integer paymentId, BigDecimal amount) {
        // Thiết lập các tham số cần thiết cho VNPay
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", "YOUR_TMN_CODE"); // Thay thế bằng mã TMN của bạn
        params.put("vnp_Amount", String.valueOf(amount.multiply(new BigDecimal(100)).intValue())); // Số tiền cần nhân với 100
        params.put("vnp_OrderInfo", "Thanh toán cho tư vấn phong thủy");
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", "http://localhost:8080/api/payments/vnpay-return");
        params.put("vnp_TxnRef", String.valueOf(paymentId));
        params.put("vnp_IpAddr", "127.0.0.1");

        // Tạo query string từ các tham số
        String queryUrl = createQueryUrl(params);
        return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + queryUrl;
    }

    // Hàm để tạo query string từ các tham số
    private String createQueryUrl(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return query.toString();
    }

    // Hàm kiểm tra trạng thái thanh toán từ VNPay
    public String checkPaymentStatus(String transactionId) {
        // Giả lập gọi VNPay API để kiểm tra trạng thái thanh toán
        // Cần thực hiện tích hợp thực tế để kiểm tra trạng thái
        return "SUCCESS"; // Giả định kết quả thanh toán thành công
    }
}

