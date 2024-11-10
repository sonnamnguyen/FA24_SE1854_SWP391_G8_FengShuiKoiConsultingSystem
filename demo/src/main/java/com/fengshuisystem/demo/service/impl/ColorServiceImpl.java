
package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ColorDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShapeDTO;
import com.fengshuisystem.demo.entity.Color;
import com.fengshuisystem.demo.entity.Destiny;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ColorMapper;
import com.fengshuisystem.demo.repository.ColorRepository;
import com.fengshuisystem.demo.repository.DestinyRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ColorServiceImpl implements ColorService {
    ColorMapper colorMapper;
    ColorRepository colorRepository;
    DestinyRepository destinyRepository;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ColorDTO createColor(ColorDTO colorDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        if(colorDTO.getDestiny() == null) throw new AppException(ErrorCode.DESTINY_NOT_EXISTED);
        if(colorRepository.existsByColor(colorDTO.getColor())) throw new AppException(ErrorCode.COLOR_EXISTED);
        Destiny destiny = destinyRepository.findById(colorDTO.getDestiny().getId()).orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        Color color = colorMapper.toEntity(colorDTO);
        color.setStatus(Status.ACTIVE);
        color.setDestiny(destiny);
        color.setCreatedBy(name);
        color.setUpdatedBy(name);
        color.setCreatedDate(Instant.now());
        color.setUpdatedDate(Instant.now());
        return colorMapper.toDto(colorRepository.save(color));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ColorDTO> getColorByName(String name, int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = colorRepository.findAllByColorAndStatusContaining(name, status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.NONE_DATA_COLOR);
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
            throw new AppException(ErrorCode.NONE_DATA_COLOR);
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
    public void deleteColor(Integer id) {
        var animalCategory = colorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_EXISTED));
        animalCategory.setStatus(Status.DELETED);
        colorRepository.save(animalCategory);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ColorDTO updateColor(Integer id, ColorDTO colorDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        if(colorDTO.getDestiny() == null) throw new AppException(ErrorCode.DESTINY_NOT_EXISTED);
        Destiny destiny = destinyRepository.findById(colorDTO.getDestiny().getId()).orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        Color color = colorRepository.findById(colorDTO.getId()).orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_EXISTED));
        if(colorRepository.existsByColor(colorDTO.getColor())) throw new AppException(ErrorCode.COLOR_EXISTED);
        color.setDestiny(destiny);
        colorMapper.update(colorDTO, color);
        color.setUpdatedDate(Instant.now());
        color.setUpdatedBy(name);
        return colorMapper.toDto(colorRepository.saveAndFlush(color));
    }

    public List<ColorDTO> getAllColors() {
        Status status = Status.ACTIVE;
        return colorRepository.findAllByStatus(status).stream().map(colorMapper::toDto).toList();
    }
    @Override
    public List<ColorDTO> getColorsByDestiny(Integer destiny) {
        List<ColorDTO> colors = colorRepository.findAllByDestiny(destiny)
                .stream()
                .map(colorMapper::toDto)
                .toList();
        if (colors.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return colors;
    }

    @Override
    public List<ColorDTO> getColorsByAnimalId(Integer animalId) {
        List<ColorDTO> colors = colorRepository.findAllByAnimal(animalId)
                .stream()
                .map(colorMapper::toDto)
                .toList();
        if (colors.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return colors;
    }
}

