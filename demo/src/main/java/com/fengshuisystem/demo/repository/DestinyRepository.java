
package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Destiny;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinyRepository extends JpaRepository<Destiny,Integer> {
    Optional<Destiny> findByDestiny(String destiny);
    @Query("SELECT DISTINCT d FROM Destiny d JOIN d.colors c JOIN c.animalCategories a WHERE a.id = :animalId")
    List<Destiny> findAllByAnimalId(@Param("animalId") Integer animalId);
    @Query("SELECT d FROM Destiny d JOIN d.shapes s WHERE s.id = :shapeId")
    Destiny findByShapeId(@Param("shapeId")Integer shapeId);
    @Query("SELECT d FROM Destiny d JOIN d.directions s WHERE s.id = :directionId")
    Destiny findByDirectionId(@Param("directionId")Integer directionId);
    @Query("SELECT d FROM Destiny d JOIN d.numbers n WHERE n.id = :numberId")
    Destiny findByNumber(@Param("numberId")Integer numberId);
}
