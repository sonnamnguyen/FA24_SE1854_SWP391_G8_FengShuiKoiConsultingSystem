package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConsultationResultRepository extends JpaRepository<ConsultationResult, Integer> {
    Page<ConsultationResult> findAll(Pageable pageable);

    @Query("SELECT cr FROM ConsultationResult cr WHERE (:name IS NULL OR TRIM(:name) = '' OR cr.consultantName LIKE CONCAT('%', :name, '%'))")
    Page<ConsultationResult> findAllByConsultantName(@Param("name") String name, Pageable pageable);

    @Query("SELECT COUNT(cr) FROM ConsultationResult cr WHERE cr.status = :status")
    long countByStatus(@Param("status") Request status);

    List<ConsultationResult> findByAccount(Account account);
}
