package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationDestiny;
import com.fengshuisystem.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationDestinyRepository extends JpaRepository<ConsultationDestiny, Integer> {
    Page<ConsultationDestiny> findAllByStatus(Status status, Pageable  pageable);
    @Query("SELECT d FROM ConsultationDestiny d WHERE d.destiny = :destiny AND d.code = :codes AND d.status = :status")
    List<ConsultationDestiny> findByCodesAndStatus(@Param("destiny") String destiny, @Param ("codes") String codes, @Param("status") Status status);
    @Query("SELECT distinct d FROM ConsultationDestiny d  WHERE d.destiny = :destiny AND d.code = :codes AND d.status = :status AND d.name = :name")
    ConsultationDestiny findByCodesAndStatusAndName(@Param ("destiny") String destiny, @Param ("codes") String codes, @Param("status") Status status, @Param ("name") String name);
}
