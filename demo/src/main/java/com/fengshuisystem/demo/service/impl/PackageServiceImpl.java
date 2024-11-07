package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.dto.PackageDTO;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.PackageMapper;
import com.fengshuisystem.demo.repository.PackageRepository;
import com.fengshuisystem.demo.service.PackageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PackageServiceImpl implements PackageService {
    PackageMapper packageMapper;
    PackageRepository packageRepository;
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PackageDTO createPackage(PackageDTO packageDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Package pkg = packageMapper.toEntity(packageDTO);
        pkg.setStatus(Status.ACTIVE);
        pkg.setCreatedBy(name);
        return packageMapper.toDto(packageRepository.save(pkg));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deletePackage(Integer id) {
        var pkg = packageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
        pkg.setStatus(Status.DELETED);
        packageRepository.save(pkg);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PackageDTO updatePackage(Integer id, PackageDTO packageDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Package pkg = packageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
        packageMapper.update(packageDTO, pkg);
        pkg.setUpdatedBy(name);
        pkg.setUpdatedDate(Instant.now());
        return packageMapper.toDto(packageRepository.save(pkg));
    }

    @Override
    public PageResponse<PackageDTO> getPackages(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = packageRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.PACKAGE_NOT_EXISTED);
        }
        return PageResponse.<PackageDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(packageMapper::toDto).toList())
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public PackageDTO findById(Integer id) {
        var pkg = packageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
        return packageMapper.toDto(pkg);
    }

}
