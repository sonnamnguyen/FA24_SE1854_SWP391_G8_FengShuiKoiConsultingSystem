package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.AnimalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalCategoryRepository extends JpaRepository<AnimalCategory, Integer> {
}