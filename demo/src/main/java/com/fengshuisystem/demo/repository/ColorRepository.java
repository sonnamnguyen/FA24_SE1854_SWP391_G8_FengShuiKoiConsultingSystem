
package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Color;
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
public interface ColorRepository extends JpaRepository<Color, Integer> {
    boolean existsByColor(String color);
    Optional<Color> findByColor(String color);
    Page<Color> findAllByStatus(Status status, Pageable pageable);
    @Query("SELECT ac FROM Color ac " +
            "WHERE ac.status = :status " +
            "AND (:name IS NULL OR TRIM(:name) = '' OR ac.color LIKE %:name%)")
    Page<Color> findAllByColorAndStatusContaining(String name, Status status, Pageable pageable);
    @Query(value = "SELECT c from Color c where c.status = 'ACTIVE'")
    List<Color> findAllByStatus(Status status);
    @Query(value = "SELECT c FROM Color c JOIN c.destiny d WHERE d.id = :destiny AND c.status = 'ACTIVE'")
    List<Color> findAllByDestiny(@Param("destinyId")Integer destiny);
    @Query(value = "SELECT c FROM Color c JOIN c.animalCategories ac WHERE ac.id = :animalId AND ac.status = 'ACTIVE'")
    List<Color> findAllByAnimal(@Param("destinyId")Integer animalId);
}
