
package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Shape;
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
public interface ShapeRepository extends JpaRepository<Shape, Integer> {
    boolean existsByShape(String shape);
    Optional<Shape> findByShape(String shape);
    Page<Shape> findAllByStatus(Status status, Pageable pageable);
    @Query("SELECT ac FROM Shape ac " +
            "WHERE ac.status = :status " +
            "AND (:name IS NULL OR TRIM(:name) = '' OR ac.shape LIKE %:name%)")
    Page<Shape> findAlByShapeNameContaining(String name, Status status, Pageable pageable);
    @Query(value = "select s from Shape s where s.status = 'ACTIVE'")
    List<Shape> findAllByStatus(Status status);
    @Query(value = "SELECT s FROM Shape s JOIN s.destiny d WHERE d.id = :destinyId AND s.status = 'ACTIVE'")
    List<Shape> findAllByDestiny(@Param("destinyId")Integer destinyId);
}
