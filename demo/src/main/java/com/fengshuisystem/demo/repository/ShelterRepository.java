package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ShelterRepository extends JpaRepository<ShelterCategory, Integer> {
    boolean existsByShelterCategoryName(String name);
    @Query("SELECT ac FROM ShelterCategory ac " +
            "WHERE ac.status = :status " +
            "AND (:name IS NULL OR TRIM(:name) = '' OR ac.shelterCategoryName LIKE %:name%)")
    Page<ShelterCategory> findAllByShelterCategoryNameAndStatusContaining(String name, Status status, Pageable pageable);
    Page<ShelterCategory> findAllByStatus(Status status, Pageable pageable);

}
