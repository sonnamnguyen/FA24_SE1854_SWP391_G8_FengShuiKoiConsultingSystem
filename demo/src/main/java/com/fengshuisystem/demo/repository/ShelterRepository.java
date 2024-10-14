package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ShelterCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShelterRepository extends JpaRepository<ShelterCategory, Integer> {
    boolean existsByShelterCategoryName(String name);
    Page<ShelterCategory> findAllByShelterCategoryName(String name, Pageable pageable);
    Page<ShelterCategory> findAllByStatus(String status, Pageable pageable);

}
