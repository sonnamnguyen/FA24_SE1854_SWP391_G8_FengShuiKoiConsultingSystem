package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PackageDTO;
import com.fengshuisystem.demo.dto.PageResponse;


public interface PackageService {
    PackageDTO createPackage(PackageDTO packageDTO);
    void deletePackage(Integer id);
    PackageDTO updatePackage(Integer id, PackageDTO packageDTO);
    PageResponse<PackageDTO> getPackages(int page, int size);
    PackageDTO findById(Integer id);

}
