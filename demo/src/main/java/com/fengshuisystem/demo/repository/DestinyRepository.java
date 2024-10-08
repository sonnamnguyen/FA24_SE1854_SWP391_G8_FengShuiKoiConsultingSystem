package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Destiny;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinyRepository  extends JpaRepository<Destiny, Integer> {
    Destiny findByDestiny(String destinyName);
    @Query("SELECT DISTINCT d FROM Destiny d JOIN d.colors c JOIN c.animalCategories a WHERE a.id = :animalId")
    List<Destiny> findAllByAnimalId(@Param("animalId") Integer animalId);
}
