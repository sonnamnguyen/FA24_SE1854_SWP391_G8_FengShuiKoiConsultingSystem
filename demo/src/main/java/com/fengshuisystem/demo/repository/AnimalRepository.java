
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
    @Query(value = "SELECT a.* FROM animal_category a " +
            "INNER JOIN animal_color ac ON a.animal_category_id = ac.animal_category_id " +
            "INNER JOIN color c ON ac.color_id = c.color_id " +
            "INNER JOIN destiny d ON c.destiny_id = d.destiny_id " +
            "WHERE d.destiny = :destiny AND a.status = :status",
            nativeQuery = true)
    Page<AnimalCategory> findActiveAnimalCategoriesByDestiny(@Param("destiny") String destiny,
                                                                  @Param("status") Status status,
                                                                  Pageable pageable);

}
