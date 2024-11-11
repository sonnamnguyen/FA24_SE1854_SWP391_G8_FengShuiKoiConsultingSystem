
package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.enums.Status;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AnimalRepository extends JpaRepository<AnimalCategory, Integer> {
    boolean existsByAnimalCategoryName(String name);
    Optional<AnimalCategory> findByAnimalCategoryName(String name);
    Page<AnimalCategory> findAllByStatus(Status status, Pageable pageable);
    @Query("SELECT ac FROM AnimalCategory ac " +
            "WHERE ac.status = :status " +
            "AND (:name IS NULL OR TRIM(:name) = '' OR ac.animalCategoryName LIKE %:name%)")
    Page<AnimalCategory> findAllByAnimalCategoryNameContainingOriginContaining(String name, Status status, Pageable pageable);
    @Query("SELECT ac FROM AnimalCategory ac JOIN ac.colors c WHERE c.id = :colorId AND ac.status = 'ACTIVE'")
    List<AnimalCategory> findAllByColorId(@Param("colorId") Integer colorId);
    @Query("SELECT DISTINCT a FROM AnimalCategory a " +
            "JOIN a.colors c " +
            "JOIN c.destiny d " +
            "WHERE d.destiny IN :destiny AND a.status = :status " +
            "ORDER BY a.id DESC")
    Page<AnimalCategory> findActiveAnimalCategoriesByDestiny(@Param("destiny") List<String> destiny,
                                                             @Param("status") Status status,
                                                             Pageable pageable);

}
