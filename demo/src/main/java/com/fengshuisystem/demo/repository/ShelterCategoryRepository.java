package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.entity.ShelterCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterCategoryRepository extends JpaRepository<ShelterCategory, Integer> {
    @Query("SELECT a FROM AnimalCategory a WHERE a.id IN :ids")
    List<ShelterCategory> findAllByIds(@Param("ids") List<Integer> ids);
}


