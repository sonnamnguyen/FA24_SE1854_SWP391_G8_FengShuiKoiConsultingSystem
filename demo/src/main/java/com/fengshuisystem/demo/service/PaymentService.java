package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.entity.Payment;

import java.util.List;


public interface PaymentService {
    List<Payment> getAllPayments();
    Payment getPaymentById(Integer id);
    Payment createPayment(Payment payment);
    Payment updatePayment(Integer id, Payment payment);
    void deletePayment(Integer id);
}
