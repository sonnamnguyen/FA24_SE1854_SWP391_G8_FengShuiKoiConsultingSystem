package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ConsulationResultRepository extends JpaRepository<ConsultationResult, Integer> {


}
