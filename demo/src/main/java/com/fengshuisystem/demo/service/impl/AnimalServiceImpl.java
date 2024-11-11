package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ColorDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.AnimalImage;
import com.fengshuisystem.demo.entity.Color;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.AnimalMapper;
import com.fengshuisystem.demo.repository.AnimalImageRepository;
import com.fengshuisystem.demo.repository.AnimalRepository;
import com.fengshuisystem.demo.repository.ColorRepository;
import com.fengshuisystem.demo.service.AnimalService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AnimalServiceImpl implements AnimalService {

    AnimalRepository animalRepository;
    AnimalMapper animalMapper;
    ColorRepository colorRepository;
    AnimalImageRepository animalImageRepository;
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AnimalCategoryDTO createAnimal(AnimalCategoryDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        if(animalRepository.existsByAnimalCategoryName(request.getAnimalCategoryName())) throw new AppException(ErrorCode.ANIMAL_EXISTED);
        if(request.getAnimalImages().isEmpty()) throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        if(request.getColors().isEmpty()) throw new AppException(ErrorCode.COLOR_NOT_EXISTED);
        Set<Color> colors = new HashSet<>();
        if (request.getColors() != null) {
            for (ColorDTO colorDTO : request.getColors()) {
                Color color = colorRepository.findById(colorDTO.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_EXISTED));
                colors.add(color);
            }
            if (colors.size() > 3) {
                throw new AppException(ErrorCode.TOO_MANY_COLORS);
            }
        } else {
            throw new AppException(ErrorCode.COLOR_NOT_EXISTED);
        }

        AnimalCategory animalCategory = animalMapper.toEntity(request);
        animalCategory.setStatus(Status.ACTIVE);
        animalCategory.setUpdatedDate(Instant.now());
        animalCategory.setCreatedDate(Instant.now());
        animalCategory.setColors(colors);
        animalCategory.setCreatedBy(name);
        animalCategory.setUpdatedBy(name);
        Set<String> uniqueUrls = new HashSet<>();
        for (AnimalImage animalImage : animalCategory.getAnimalImages()) {
            String imageUrl = animalImage.getImageUrl();
            if (!uniqueUrls.add(imageUrl)) {
                throw new AppException(ErrorCode.PICK_SAME_IMAGE);
            }
            if(animalImageRepository.existsByImageUrl(animalImage.getImageUrl())) throw new AppException(ErrorCode.IMAGE_ALREADY_EXISTED);
            animalImage.setAnimalCategory(animalCategory);
        }
        return animalMapper.toDto(animalRepository.save(animalCategory));
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AnimalCategoryDTO> getAnimalsBySearch(String search, int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = animalRepository.findAllByAnimalCategoryNameContainingOriginContaining(search, status,pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.NONE_DATA_ANIMAL);
        }

        return PageResponse.<AnimalCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(animalMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public PageResponse<AnimalCategoryDTO> getAnimalsByDestiny(List<String> destiny, int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = animalRepository.findActiveAnimalCategoriesByDestiny(destiny, status,pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.NONE_DATA_ANIMAL);
        }

        return PageResponse.<AnimalCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(animalMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AnimalCategoryDTO> getAnimals(int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = animalRepository.findAllByStatus(status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.NONE_DATA_ANIMAL);
        }
        return PageResponse.<AnimalCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(animalMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAnimal(Integer id) {
        var animalCategory = animalRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        animalCategory.setStatus(Status.DELETED);
        animalRepository.save(animalCategory);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AnimalCategoryDTO updateAnimal(Integer id, AnimalCategoryDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        AnimalCategory animalCategory = animalRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        if(animalRepository.existsByAnimalCategoryName(request.getAnimalCategoryName())) throw new AppException(ErrorCode.ANIMAL_EXISTED);
        if(request.getAnimalImages().isEmpty()) throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        if(request.getColors().isEmpty()) throw new AppException(ErrorCode.COLOR_NOT_EXISTED);
        animalMapper.update(request, animalCategory);
        Set<Color> colors = new HashSet<>();
        if (request.getColors() != null) {
            for (ColorDTO colorDTO : request.getColors()) {
                Color color = colorRepository.findById(colorDTO.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_EXISTED));
                colors.add(color);
            }
            if (colors.size() > 3) {
                throw new AppException(ErrorCode.TOO_MANY_COLORS);
            }
        } else {
            throw new AppException(ErrorCode.COLOR_NOT_EXISTED);
        }
        Set<String> uniqueUrls = new HashSet<>();
        for (AnimalImage animalImage : animalCategory.getAnimalImages()) {
            String imageUrl = animalImage.getImageUrl();
            if (!uniqueUrls.add(imageUrl)) {
                throw new AppException(ErrorCode.PICK_SAME_IMAGE);
            }
            if(animalImageRepository.existsByImageUrl(animalImage.getImageUrl())) throw new AppException(ErrorCode.IMAGE_ALREADY_EXISTED);
            animalImage.setAnimalCategory(animalCategory);
        }
        animalCategory.setUpdatedBy(name);
        animalCategory.setColors(colors);
        animalCategory.setUpdatedDate(Instant.now());
        return animalMapper.toDto(animalRepository.saveAndFlush(animalCategory));
    }

    @Override
    public List<AnimalCategoryDTO> getAnimalCategoryByColorId(int color) {
        return animalRepository.findAllByColorId(color)
                .stream()
                .map(animalMapper::toDto)
                .toList();
    }

    @Override
    public AnimalCategoryDTO getAnimalById(Integer id) {
        AnimalCategory animalCategory = animalRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        return animalMapper.toDto(animalCategory);
    }

    @Override
    public List<AnimalCategoryDTO> getAllAnimalCategory() {
        List<AnimalCategoryDTO> animalCategoryDTOS = animalRepository.findAll()
                .stream()
                .map(animalMapper::toDto)
                .toList();
        if (animalCategoryDTOS.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return animalCategoryDTOS;
    }
}
