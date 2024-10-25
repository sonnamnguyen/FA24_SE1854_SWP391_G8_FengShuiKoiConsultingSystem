package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRequestDetailRepository extends JpaRepository<ConsultationRequestDetail, Integer> {
    @Query(value = "SELECT d.* " +
            "FROM consultation_result c " +
            "JOIN consultation_request_detail d " +
            "ON c.request_detail_consultation_request_detail_id = d.consultation_request_detail_id " +
            "WHERE d.consultation_request_detail_id = :requestDetailId", nativeQuery = true)
    public ConsultationRequestDetail findByRequestDetailId( Integer requestDetailId);
}
