package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.repository.PackageRepository;
import com.fengshuisystem.demo.service.PackageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepository;

    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    @Override
    public Package getPackageById(Integer id) {
        return packageRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
    }

    @Override
    public Package createPackage(Package pkg) {
        return packageRepository.save(pkg);
    }

    @Override
    public Package updatePackage(Integer id, Package pkg) {
        Optional<Package> existingPackage = packageRepository.findById(id);
        if (existingPackage.isPresent()) {
            Package updatedPackage = existingPackage.get();
            updatedPackage.setPackageName(pkg.getPackageName());
            updatedPackage.setPrice(pkg.getPrice());
            updatedPackage.setDescription(pkg.getDescription());
            updatedPackage.setDuration(pkg.getDuration());
            updatedPackage.setCreateDate(pkg.getCreateDate());
            updatedPackage.setUpdateDate(pkg.getUpdateDate());
            updatedPackage.setCreateBy(pkg.getCreateBy());
            updatedPackage.setUpdateBy(pkg.getUpdateBy());
            updatedPackage.setBills(pkg.getBills());
            return packageRepository.save(updatedPackage);
        } else {
            throw new AppException(ErrorCode.PACKAGE_NOT_EXISTED);
        }
    }

    @Override
    public void deletePackage(Integer id) {
        Optional<Package> existingPackage = packageRepository.findById(id);
        if (existingPackage.isPresent()) {
            packageRepository.deleteById(id);}
        else {
            throw new AppException(ErrorCode.PACKAGE_NOT_EXISTED);
        }
    }
}
