package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsulationAnimalRepository extends JpaRepository<ConsultationAnimal, Integer> {
}
