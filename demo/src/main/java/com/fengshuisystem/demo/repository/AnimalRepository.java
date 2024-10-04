package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AnimalRepository extends JpaRepository<AnimalCategory, Integer> {
    boolean existsByAnimalCategoryName(String name);
//    Page<AnimalCategory> findAllByStatus(Status status, Pageable pageable);
    @Query(value = "SELECT a FROM AnimalCategory a " +
            "JOIN a.animalImages ai " +
            "JOIN a.colors c " +
            "WHERE a.status = 'ACTIVE' AND " +
            "(:#{#dto.animalCategoryName} IS NULL OR a.animalCategoryName LIKE %:#{#dto.animalCategoryName}%) AND " +
            "(:#{#dto.origin} IS NULL OR a.origin LIKE %:#{#dto.origin}%) AND " +
            "(:#{#dto.createdDate} IS NULL OR a.createdDate = :#{#dto.createdDate}) AND " +
            "(:#{#dto.colors} IS NULL OR :#{#dto.colors} member of a.colors)"
    )
    Page<AnimalCategory> findAllBySearch(AnimalCategoryDTO dto, Pageable pageable);




}
