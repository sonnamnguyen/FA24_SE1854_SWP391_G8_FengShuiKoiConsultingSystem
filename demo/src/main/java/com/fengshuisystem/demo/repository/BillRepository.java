package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Page<Bill> findAllByStatus(BillStatus status, Pageable pageable);
    Page<Bill> findAllByAccount_IdAndStatus(int accountId, BillStatus status, Pageable pageable);

}
