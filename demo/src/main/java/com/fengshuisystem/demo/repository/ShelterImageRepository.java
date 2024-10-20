package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ShelterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterImageRepository extends JpaRepository<ShelterImage, Integer> {
     @Query(value = "select s from ShelterImage s where s.shelterCategory.id = :shelterId")
     List<ShelterImage> findByShelterCategory(Integer shelterId);
}
