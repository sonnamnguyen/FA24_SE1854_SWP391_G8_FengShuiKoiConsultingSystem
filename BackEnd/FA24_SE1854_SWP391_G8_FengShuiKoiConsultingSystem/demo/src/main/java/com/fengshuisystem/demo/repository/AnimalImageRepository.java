package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.AnimalImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalImageRepository extends JpaRepository<AnimalImage, Integer> {
     @Query(value = "SELECT * FROM animal_image " +
             "WHERE animal_category_id = :animalId " +
             "AND image_url IN (:imgUrls)", nativeQuery = true)
     List<AnimalImage> findByAnimalCategoryAndImgUrls(@Param("animalId") Integer animalId, @Param("imgUrls") List<String> imgUrls);
     boolean existsByImageUrl(String imageUrl);
}