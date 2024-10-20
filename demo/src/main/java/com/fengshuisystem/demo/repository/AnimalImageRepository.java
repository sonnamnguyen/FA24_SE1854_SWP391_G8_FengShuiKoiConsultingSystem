package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.AnimalImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalImageRepository extends JpaRepository<AnimalImage, Integer> {
     @Query( value = "SELECT a FROM AnimalImage a WHERE a.animalCategory.id = :animalId")
     List<AnimalImage> findByAnimalCategory(Integer animalId);
}
