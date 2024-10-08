package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.entity.Payment;
import com.fengshuisystem.demo.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/payments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;
    // Lấy tất cả Payment
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments); // Trả về mã trạng thái 200 (OK)
    }

    // Lấy Payment theo ID
    @GetMapping("/{id}")
    public ApiResponse<Payment> getPaymentById(@PathVariable Integer id) {
        return ApiResponse.<Payment>builder()
                .result(paymentService.getPaymentById(id))
                .build();
    }

    // Tạo mới Payment
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.status(201).body(createdPayment); // Trả về mã trạng thái 201 (Created)
    }

    // Cập nhật Payment theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Integer id, @RequestBody Payment paymentDetails) {
        Payment updatedPayment = paymentService.updatePayment(id, paymentDetails);
        return ResponseEntity.ok(updatedPayment); // Trả về mã trạng thái 200 (OK)
    }

    // Xóa Payment theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build(); // Trả về mã trạng thái 204 (No Content)

    }
}
