package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConsultationResultRepository extends JpaRepository<ConsultationResult, Integer> {

    public Page<ConsultationResult> findAllByStatus(Status status, Pageable pageable);
}
