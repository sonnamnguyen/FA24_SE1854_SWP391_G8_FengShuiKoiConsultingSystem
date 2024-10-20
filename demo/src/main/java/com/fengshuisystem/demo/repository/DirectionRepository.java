package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Integer> {
    Set<Direction> findAllByIdIn(List<Integer> ids);

}
