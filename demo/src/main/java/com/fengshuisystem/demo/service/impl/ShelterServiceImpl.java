package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ShelterMapper;
import com.fengshuisystem.demo.repository.ShapeRepository;
import com.fengshuisystem.demo.repository.ShelterImageRepository;
import com.fengshuisystem.demo.repository.ShelterRepository;
import com.fengshuisystem.demo.service.ShelterService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShelterServiceImpl implements ShelterService {

    ShelterRepository shelterRepository;
    ShelterMapper shelterMapper;
    ShapeRepository shapeRepository;
    ShelterImageRepository shelterImageRepository;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ShelterCategoryDTO createShelter(ShelterCategoryDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        if(shelterRepository.existsByShelterCategoryName(request.getShelterCategoryName())) throw new AppException(ErrorCode.SHELTER_EXISTED);
        if(request.getShelterImages().isEmpty()) throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        if(request.getShape() == null) throw new AppException(ErrorCode.SHAPE_NOT_EXISTED);
        Shape shape = shapeRepository.findById(request.getShape().getId()).orElseThrow(() -> new AppException(ErrorCode.SHAPE_NOT_EXISTED));
        ShelterCategory shelterCategory = shelterMapper.toEntity(request);
        shelterCategory.setStatus(Status.ACTIVE);
        shelterCategory.setCreatedDate(Instant.now());
        shelterCategory.setCreatedBy(name);
        shelterCategory.setUpdatedDate(Instant.now());
        shelterCategory.setUpdatedBy(name);
        shelterCategory.setShape(shape);
        Set<String> uniqueUrls = new HashSet<>();
        for (ShelterImage shelterImage : shelterCategory.getShelterImages()) {
            String imageUrl = shelterImage.getImageUrl();
            if (!uniqueUrls.add(imageUrl)) {
                throw new AppException(ErrorCode.PICK_SAME_IMAGE);
            }
            if(shelterImageRepository.existsByImageUrl(shelterImage.getImageUrl())) throw new AppException(ErrorCode.IMAGE_ALREADY_EXISTED);
            shelterImage.setShelterCategory(shelterCategory);
        }
        return shelterMapper.toDto(shelterRepository.save(shelterCategory));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ShelterCategoryDTO> getSheltersBySearch(String name, int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = shelterRepository.findAllByShelterCategoryNameAndStatusContaining(name, status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.NONE_DATA_SHELTER);
        }
        return PageResponse.<ShelterCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(shelterMapper::toDto).toList())
                .build();
    }
    @Override
    @PreAuthorize("hasRole('USER')")
   public PageResponse<ShelterCategoryDTO> getSheltersByDestiny(List<String> destiny, int page, int size){
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = shelterRepository.findShelterCategoriesByDestinyAndStatus(destiny, status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.NONE_DATA_SHELTER);
        }
        return PageResponse.<ShelterCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(shelterMapper::toDto).toList())
                .build();
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ShelterCategoryDTO> getAllShelters(int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = shelterRepository.findAllByStatus(status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.NONE_DATA_SHELTER);
        }
        return PageResponse.<ShelterCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(shelterMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteShelter(Integer id) {
        var shelterCategory = shelterRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHELTER_NOT_EXISTED));
        shelterCategory.setStatus(Status.DELETED);
        shelterRepository.save(shelterCategory);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ShelterCategoryDTO updateShelter(Integer id, ShelterCategoryDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        ShelterCategory shelterCategory = shelterRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHELTER_NOT_EXISTED));
        if(shelterRepository.existsByShelterCategoryName(request.getShelterCategoryName())) throw new AppException(ErrorCode.SHELTER_EXISTED);
        if(request.getShelterImages().isEmpty()) throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        if(request.getShape() == null) throw new AppException(ErrorCode.SHAPE_NOT_EXISTED);
        Shape shape = shapeRepository.findById(request.getShape().getId()).orElseThrow(() -> new AppException(ErrorCode.SHAPE_NOT_EXISTED));
        shelterMapper.update(request, shelterCategory);
        shelterCategory.setShape(shape);
        shelterCategory.setUpdatedDate(Instant.now());
        shelterCategory.setUpdatedBy(name);
        Set<String> uniqueUrls = new HashSet<>();
        for (ShelterImage shelterImage : shelterCategory.getShelterImages()) {
            String imageUrl = shelterImage.getImageUrl();
            if (!uniqueUrls.add(imageUrl)) {
                throw new AppException(ErrorCode.PICK_SAME_IMAGE);
            }
            if(shelterImageRepository.existsByImageUrl(shelterImage.getImageUrl())) throw new AppException(ErrorCode.IMAGE_ALREADY_EXISTED);
            shelterImage.setShelterCategory(shelterCategory);
        }
        return shelterMapper.toDto(shelterRepository.save(shelterCategory));
    }

    @Override
    public List<ShelterCategoryDTO> getAllSheltersByShape(Integer shape) {
        return shelterRepository.findAllByShape(shape)
                .stream()
                .map(shelterMapper::toDto)
                .toList();
    }

    @Override
    public ShelterCategoryDTO getShelterById(Integer id) {
        ShelterCategory shelterCategory = shelterRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHELTER_NOT_EXISTED));
        return shelterMapper.toDto(shelterCategory);
    }

    @Override
    public List<ShelterCategoryDTO> getAllShelterCategory() {
        List<ShelterCategoryDTO> shelterCategoryDTOS = shelterRepository.findAll()
                .stream()
                .map(shelterMapper::toDto)
                .toList();
        if (shelterCategoryDTOS.isEmpty()) {
            throw new AppException(ErrorCode.SHELTER_NOT_EXISTED);
        }
        return shelterCategoryDTOS;
    }
}
