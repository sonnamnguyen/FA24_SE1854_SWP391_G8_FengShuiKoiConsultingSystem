package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationShelter;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationShelterRepository extends JpaRepository<ConsultationShelter, Integer> {
    Optional<ConsultationShelter> findByConsultationResultIdAndShelterCategoryId(Integer resultId, Integer shelterCategoryId);

    List<ConsultationShelter> findByConsultationResult(ConsultationResult consultationResult);

    List<ConsultationShelter> findByConsultationResultId(Integer resultId);
}
