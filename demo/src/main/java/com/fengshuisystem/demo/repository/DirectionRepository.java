package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Color;
import com.fengshuisystem.demo.entity.Direction;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Integer> {
    @Query(value = "SELECT r FROM Direction r JOIN r.destiny d WHERE d.id = :destiny")
    List<Direction> findAllByDestiny(@Param("destinyId") Integer destiny);
}
