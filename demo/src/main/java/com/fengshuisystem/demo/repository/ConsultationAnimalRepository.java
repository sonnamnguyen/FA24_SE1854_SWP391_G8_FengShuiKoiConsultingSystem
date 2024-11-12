package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationAnimal;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationAnimalRepository extends JpaRepository<ConsultationAnimal, Integer> {
    List<ConsultationAnimal> findByConsultationResult(ConsultationResult consultationResult);

    Optional<ConsultationAnimal> findByConsultationResultIdAndAnimalCategoryId(Integer resultId, Integer animalCategoryId);

    List<ConsultationAnimal> findByConsultationResultId(Integer resultId);

}
