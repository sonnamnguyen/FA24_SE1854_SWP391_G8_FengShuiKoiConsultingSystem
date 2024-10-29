package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.DestinyYear;
import com.fengshuisystem.demo.entity.enums.Status;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinyYearRepository extends JpaRepository<DestinyYear, Integer> {
    List<DestinyYear> findAllByStatus(Status status);
    Page<DestinyYear> findAllByStatus(Status status, Pageable pageable);
    @Query("SELECT d from DestinyYear d WHERE d.year =:year AND d.status=:status")
    DestinyYear findByYear(@Param("year")Integer year, @Param("status") Status status);
}
