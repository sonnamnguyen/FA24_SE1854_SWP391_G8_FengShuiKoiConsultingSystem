package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.configuration.VnPayConfig;
import com.fengshuisystem.demo.dto.PaymentResDTO;
import com.fengshuisystem.demo.dto.TransactionStatusDTO;
import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.repository.BillRepository;
import com.fengshuisystem.demo.repository.ConsultationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fengshuisystem.demo.configuration.VnPayConfig.orderType;

@RestController
@RequestMapping("/vn_pay")
public class VnPayController {

    private final BillRepository billRepository;
    private final ConsultationRequestRepository consultationRequestRepository;

    public VnPayController(BillRepository billRepository, ConsultationRequestRepository consultationRequestRepository) {
        this.billRepository = billRepository;
        this.consultationRequestRepository = consultationRequestRepository;
    }

    @GetMapping("/vnpay_infor")
    public ResponseEntity<?> handleVnPayResponse(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String orderInfo,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "billId") Integer billId // Nhận thêm billId từ URL
    ) {
        if (responseCode.equals("00")) { // Kiểm tra nếu thanh toán thành công
            try {
                // Cập nhật trạng thái của hóa đơn thành COMPLETED
                Bill bill = billRepository.findById(billId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Bill với ID: " + billId));
                bill.setStatus(BillStatus.PAID);
                billRepository.save(bill);

                // Cập nhật trạng thái yêu cầu tư vấn thành COMPLETED
                ConsultationRequest consultationRequest = consultationRequestRepository.findByBillId(billId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tư vấn cho Bill ID: " + billId));
                consultationRequest.setStatus(Request.COMPLETED);
                consultationRequestRepository.save(consultationRequest);

                return ResponseEntity.ok("Thanh toán thành công và cập nhật trạng thái!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật trạng thái!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán thất bại!");
        }
    }


    // Phần của Khôi
    @GetMapping("/create_vn_pay")
    public ResponseEntity<?> createVnPay2(@RequestParam(value = "amount") long amount, HttpServletRequest req) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Số tiền không hợp lệ!");
        }

        try {
            amount = amount * 100;

            String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
            String vnp_IpAddr = VnPayConfig.getIpAddress(req);
            String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_OrderType", VnPayConfig.orderType);
            vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);

            // Tạo thời gian tạo giao dịch và thời gian hết hạn
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

            cld.add(Calendar.MINUTE, 15); // Thêm thời gian hết hạn là 15 phút
            vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

            // Sắp xếp các tham số
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            // Tạo chuỗi query và hash
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    query.append('&');
                    hashData.append('&');
                }
            }

            // Xóa ký tự '&' cuối cùng
            if (query.length() > 0) query.setLength(query.length() - 1);
            if (hashData.length() > 0) hashData.setLength(hashData.length() - 1);

            // Tạo mã bảo mật (secure hash)
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            // Tạo URL thanh toán
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + query.toString();

            // Trả về URL trong response
            PaymentResDTO paymentResDTO = new PaymentResDTO();
            paymentResDTO.setStatus("OK");
            paymentResDTO.setMessage("Successfully");
            paymentResDTO.setURL(paymentUrl);

            return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tạo liên kết thanh toán!");
        }
    }
}