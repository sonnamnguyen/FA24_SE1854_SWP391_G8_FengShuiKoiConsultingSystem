package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationShelter;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationShelterRepository extends JpaRepository<ConsultationShelter, Integer> {
    boolean existsByConsultationResultIdAndShelterCategoryId(Integer resultId, Integer shelterCategoryId);

    List<ConsultationShelter> findByConsultationResult(ConsultationResult consultationResult);
}
