package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Integer> {
    @Query("SELECT cr FROM ConsultationRequest cr JOIN cr.bills b WHERE b.id = :billId")
    Optional<ConsultationRequest> findByBillId(@Param("billId") Integer billId);
    @Query("SELECT COUNT(c) FROM ConsultationRequest c WHERE c.status = 'COMPLETED'")
    long countCompletedConsultationRequests();

    List<ConsultationRequest> findByFullNameContainingIgnoreCase(String fullName);
    List<ConsultationRequest> findByEmailContainingIgnoreCase(String email);
    List<ConsultationRequest> findByPhoneContainingIgnoreCase(String phone);
}
