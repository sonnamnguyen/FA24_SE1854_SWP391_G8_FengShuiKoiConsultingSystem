package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {
    Optional<Package> findByIdAndStatus(Integer id, String status);
}

