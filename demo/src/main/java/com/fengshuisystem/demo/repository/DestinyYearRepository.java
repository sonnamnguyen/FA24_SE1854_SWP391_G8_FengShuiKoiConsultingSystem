package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.DestinyYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DestinyYearRepository extends JpaRepository<DestinyYear, Integer> {
    DestinyYear findByYear(Integer year);
}
