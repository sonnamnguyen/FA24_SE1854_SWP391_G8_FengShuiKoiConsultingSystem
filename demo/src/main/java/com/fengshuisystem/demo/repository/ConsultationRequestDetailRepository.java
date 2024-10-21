package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRequestDetailRepository
        extends JpaRepository<ConsultationRequestDetail, Integer> {
}

