package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationShelter;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationShelterRepository extends JpaRepository<ConsultationShelter, Integer> {
    boolean existsByConsultationResultIdAndShelterCategoryId(Integer resultId, Integer shelterCategoryId);

    List<ConsultationShelter> findByConsultationResult(ConsultationResult consultationResult);
}
