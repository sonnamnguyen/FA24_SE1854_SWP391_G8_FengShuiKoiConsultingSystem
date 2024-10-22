
package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.dto.ShapeDTO;
import com.fengshuisystem.demo.entity.Shape;
import com.fengshuisystem.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShapeRepository extends JpaRepository<Shape, Integer> {
    boolean existsByShape(String shape);
    Page<Shape> findAllByStatus(Status status, Pageable pageable);
    @Query(value = "select s from Shape s where s.status = 'ACTIVE'")
    Page<Shape> findAlByShape(String shape, Pageable pageable);
    @Query(value = "select s from Shape s where s.status = 'ACTIVE'")
    List<Shape> findAllByStatus(Status status);
}
