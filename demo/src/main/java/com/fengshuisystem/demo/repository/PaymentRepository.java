package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
