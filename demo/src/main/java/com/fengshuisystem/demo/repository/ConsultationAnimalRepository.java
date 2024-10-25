package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationAnimal;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationAnimalRepository extends JpaRepository<ConsultationAnimal, Integer> {
    List<ConsultationAnimal> findByConsultationResult(ConsultationResult consultationResult);
}
