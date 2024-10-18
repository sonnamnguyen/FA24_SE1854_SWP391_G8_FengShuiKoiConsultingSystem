package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.DestinyYear;
import com.fengshuisystem.demo.entity.enums.Status;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinyYearRepository extends JpaRepository<DestinyYear, Integer> {
    @Query("SELECT d from DestinyYear d WHERE d.year =:year AND d.status=:status")
    DestinyYear findByYear(@Param("year")Integer year, @Param("status") Status status);
}
