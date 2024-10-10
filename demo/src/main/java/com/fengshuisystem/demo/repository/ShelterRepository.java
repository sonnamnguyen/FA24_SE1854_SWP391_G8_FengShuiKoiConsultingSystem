package com.fengshuisystem.demo.repository;


import com.fengshuisystem.demo.entity.ShelterCategory;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShelterRepository extends JpaRepository<ShelterCategory, Integer> {
    boolean existsByShelterCategoryName(String name);
    Page<ShelterCategory> findAllByShelterCategoryName(String name, Pageable pageable);
    Page<ShelterCategory> findAllByStatus(String status, Pageable pageable);
    @Query("SELECT sc FROM ShelterCategory sc JOIN sc.shape s WHERE s.id = :shapeId AND sc.status = 'ACTIVE' ")
    List<ShelterCategory> findAllByShape(@Param("shapeId") Integer shapeId);
}
