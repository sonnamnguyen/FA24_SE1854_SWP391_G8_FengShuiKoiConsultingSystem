package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Destiny;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinyRepository extends JpaRepository<Destiny,Integer> {
}
