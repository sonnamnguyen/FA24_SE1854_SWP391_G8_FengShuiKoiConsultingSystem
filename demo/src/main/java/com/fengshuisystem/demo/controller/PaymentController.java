package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.entity.Payment;
import com.fengshuisystem.demo.repository.PaymentRepository;
import com.fengshuisystem.demo.service.VNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayService vnPayService;
    private final PaymentRepository paymentRepository;

    // Tạo đơn thanh toán
    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestParam Integer packageId, @RequestParam BigDecimal amount) {
        // Tạo bản ghi thanh toán trong database
        Payment payment = new Payment();
        payment.setPaymentMethod("VNPay");
        payment.setPaymentStatus("PENDING");
        payment.setCreatedDate(Instant.now());
        payment.setUpdatetedDate(Instant.now());
        payment.setCreatedBy("User"); // Thông tin người tạo (giả lập)
        payment.setUpdatetedBy("User");
        paymentRepository.save(payment);

        // Lấy URL thanh toán từ VNPay
        String paymentURL = vnPayService.createPaymentURL(payment.getId(), amount);

        return ResponseEntity.ok(paymentURL);
    }

    // Kiểm tra trạng thái thanh toán
    @GetMapping("/status")
    public ResponseEntity<String> checkPaymentStatus(@RequestParam String transactionId) {
        String status = vnPayService.checkPaymentStatus(transactionId);

        // Cập nhật trạng thái thanh toán vào database
        Payment payment = paymentRepository.findById(Integer.parseInt(transactionId))
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);

        return ResponseEntity.ok("Trạng thái thanh toán: " + status);
    }
}
