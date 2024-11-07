package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT b FROM Bill b WHERE b.consultationRequest.id = :consultationRequestId")
    List<Bill> findAllByConsultationRequestId(@Param("consultationRequestId") Integer consultationRequestId);
}

