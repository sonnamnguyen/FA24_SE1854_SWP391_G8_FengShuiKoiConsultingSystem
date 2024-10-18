package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationDestiny;
import com.fengshuisystem.demo.entity.enums.Status;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationDestinyRepository extends JpaRepository<ConsultationDestiny, Integer> {
    @Query("SELECT d FROM ConsultationDestiny d WHERE d.destiny = :destiny AND d.code = :codes AND d.status = :status")
    List<ConsultationDestiny> findByCodesAndStatus(@Param("destiny") String destiny, @Param ("codes") String codes, @Param("status") Status status);
    @Query("SELECT distinct d FROM ConsultationDestiny d  WHERE d.destiny = :destiny AND d.code = :codes AND d.status = :status AND d.name = :name")
    ConsultationDestiny findByCodesAndStatusAndName(@Param ("destiny") String destiny, @Param ("codes") String codes, @Param("status") Status status, @Param ("name") String name);
}
