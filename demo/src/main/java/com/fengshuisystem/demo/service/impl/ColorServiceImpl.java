package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ColorDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.Color;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ColorMapper;
import com.fengshuisystem.demo.repository.ColorRepository;
import com.fengshuisystem.demo.service.ColorService;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ColorServiceImpl implements ColorService {
    ColorMapper colorMapper;
    ColorRepository colorRepository;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ColorDTO createColor(ColorDTO colorDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        if(colorRepository.existsByColor(colorDTO.getColor())) throw new AppException(ErrorCode.ANIMAL_EXISTED);
        Color color = colorMapper.toEntity(colorDTO);
        color.setStatus(Status.ACTIVE);
        color.setCreatedBy(name);
        return colorMapper.toDto(colorRepository.save(color));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ColorDTO> getColorByName(String name, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = colorRepository.findAllByColor(name, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return PageResponse.<ColorDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(colorMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ColorDTO> getColors(int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = colorRepository.findAllByStatus(status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return PageResponse.<ColorDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(colorMapper::toDto).toList())
                .build();
    }

    @Override
    public void deleteColor(Integer id) {
        var animalCategory = colorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        animalCategory.setStatus(Status.DELETED);
        colorRepository.save(animalCategory);
    }

    @Override
    public ColorDTO updateColor(Integer id, ColorDTO colorDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Color color = colorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        colorMapper.update(colorDTO, color);
        color.setUpdatedBy(name);
        color.setUpdatedDate(Instant.now());
        return colorMapper.toDto(colorRepository.save(color));
    }
}
