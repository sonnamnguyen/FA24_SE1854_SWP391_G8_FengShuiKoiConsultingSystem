package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Integer> {
    @Query("SELECT cr FROM ConsultationRequest cr LEFT JOIN FETCH cr.bills WHERE cr.id = :id")
    Optional<ConsultationRequest> findByIdWithBills(@Param("id") Integer id);

    @Query("SELECT cr FROM ConsultationRequest cr JOIN cr.bills b WHERE b.id = :billId")
    Optional<ConsultationRequest> findByBillId(@Param("billId") Integer billId);


}
