package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Number;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NumberRepository extends JpaRepository<Number, Integer> {
    @Query(value = "SELECT r FROM Number r JOIN r.destiny d WHERE d.id = :destiny")
    List<Number> findAllByDestiny(@Param("destinyId") Integer destiny);
}
