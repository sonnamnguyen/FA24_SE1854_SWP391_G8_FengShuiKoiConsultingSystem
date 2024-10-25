package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Integer> {
}
