package com.fengshuisystem.demo.service;

import java.util.List;
import java.util.Optional;

public interface PackageService {
    List<com.fengshuisystem.demo.entity.Package> getAllPackages();
    com.fengshuisystem.demo.entity.Package getPackageById(Integer id);
    com.fengshuisystem.demo.entity.Package createPackage(com.fengshuisystem.demo.entity.Package pkg);
    com.fengshuisystem.demo.entity.Package updatePackage(Integer id, com.fengshuisystem.demo.entity.Package pkg);
    void deletePackage(Integer id);
}
