package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Optional<Bill> findByIdAndStatus(Integer id, BillStatus status);
}
