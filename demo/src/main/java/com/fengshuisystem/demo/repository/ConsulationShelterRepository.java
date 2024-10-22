package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConsulationShelterRepository extends JpaRepository<ConsultationShelter, Integer> {


}
