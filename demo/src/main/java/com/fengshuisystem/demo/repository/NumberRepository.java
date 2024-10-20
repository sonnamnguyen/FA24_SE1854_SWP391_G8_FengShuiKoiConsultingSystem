package com.fengshuisystem.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fengshuisystem.demo.entity.Number;

import java.util.List;
import java.util.Set;

@Repository
public interface NumberRepository extends JpaRepository<Number, Integer> {
    Set<Number> findAllByIdIn(List<Integer> ids);
}
