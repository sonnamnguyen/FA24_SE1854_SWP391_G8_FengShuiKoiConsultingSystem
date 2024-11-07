package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PackageDTO;
import com.fengshuisystem.demo.dto.PageResponse;


public interface PackageService {
    public PackageDTO createPackage(PackageDTO packageDTO);
    public void deletePackage(Integer id);
    public PackageDTO updatePackage(Integer id, PackageDTO packageDTO);
    public PageResponse<PackageDTO> getPackages(int page, int size);
    PackageDTO findById(Integer id);

}
