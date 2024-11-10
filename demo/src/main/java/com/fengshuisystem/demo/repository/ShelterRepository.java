
package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.enums.Status;
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
    @Query("SELECT ac FROM ShelterCategory ac " +
            "WHERE ac.status = :status " +
            "AND (:name IS NULL OR TRIM(:name) = '' OR ac.shelterCategoryName LIKE %:name%)")
    Page<ShelterCategory> findAllByShelterCategoryNameAndStatusContaining(String name, Status status, Pageable pageable);
    Page<ShelterCategory> findAllByStatus(Status status, Pageable pageable);
    @Query("SELECT sc FROM ShelterCategory sc JOIN sc.shape s WHERE s.id = :shapeId AND sc.status = 'ACTIVE' ")
    List<ShelterCategory> findAllByShape(@Param("shapeId") Integer shapeId);
    @Query(value = "SELECT s.* FROM shelter_category s " +
            "INNER JOIN shape sa ON s.shape_id = sa.shape_id " +
            "INNER JOIN destiny d ON sa.destiny_id = d.destiny_id " +
            "WHERE d.destiny = :destiny AND s.status = :status",
            nativeQuery = true)
    Page<ShelterCategory> findShelterCategoriesByDestinyAndStatus(@Param("destiny") String destiny,
                                                                  @Param("status") String status,
                                                                  Pageable pageable);

}
