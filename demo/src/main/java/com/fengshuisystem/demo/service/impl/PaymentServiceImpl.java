package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.entity.Payment;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.repository.PaymentRepository;
import com.fengshuisystem.demo.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Integer id, Payment payment) {
        Optional<Payment> existingPayment = paymentRepository.findById(id);
        if (existingPayment.isPresent()) {
            Payment updatedPayment = existingPayment.get();
            updatedPayment.setPaymentMethod(payment.getPaymentMethod());
            updatedPayment.setPaymentDate(payment.getPaymentDate());
            updatedPayment.setPaymentStatus(payment.getPaymentStatus());
            updatedPayment.setStatus(payment.getStatus());
            updatedPayment.setCreateDate(payment.getCreateDate());
            updatedPayment.setUpdateDate(payment.getUpdateDate());
            updatedPayment.setCreateBy(payment.getCreateBy());
            updatedPayment.setUpdateBy(payment.getUpdateBy());
            updatedPayment.setBills(payment.getBills());
            return paymentRepository.save(updatedPayment);
        } else {
            // Xử lý nếu không tìm thấy Payment (ví dụ: ném ngoại lệ)
            throw new AppException(ErrorCode.PAYMENT_NOT_EXISTED);
        }
    }

    @Override
    public void deletePayment(Integer id) {
        Optional<Payment> existingPayment = paymentRepository.findById(id);
        if (existingPayment.isPresent()) {
            paymentRepository.deleteById(id);}
        else {
            throw new AppException(ErrorCode.PAYMENT_NOT_EXISTED);
        }
    }
}
