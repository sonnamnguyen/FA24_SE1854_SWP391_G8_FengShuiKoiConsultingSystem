package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
