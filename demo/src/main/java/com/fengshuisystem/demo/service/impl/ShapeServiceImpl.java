package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShapeDTO;
import com.fengshuisystem.demo.entity.Destiny;
import com.fengshuisystem.demo.entity.Shape;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ShapeMapper;
import com.fengshuisystem.demo.repository.DestinyRepository;
import com.fengshuisystem.demo.repository.ShapeRepository;
import com.fengshuisystem.demo.service.ShapeService;
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
public class ShapeServiceImpl implements ShapeService {
    ShapeMapper shapeMapper;
    ShapeRepository shapeRepository;
    DestinyRepository destinyRepository;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ShapeDTO createShape(ShapeDTO shapeDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        if(shapeRepository.existsByShape((shapeDTO.getShape()))) throw new AppException(ErrorCode.ANIMAL_EXISTED);
        Destiny destiny = destinyRepository.findById(shapeDTO.getDestiny().getId()).orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        Shape shape = shapeMapper.toEntity(shapeDTO);
        shape.setStatus(Status.ACTIVE);
        shape.setCreatedBy(name);
        shape.setUpdatedBy(name);
        shape.setUpdatedDate(Instant.now());
        shape.setCreatedDate(Instant.now());
        shape.setDestiny(destiny);
        return shapeMapper.toDto(shapeRepository.save(shape));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ShapeDTO> getShapeByName(String name, int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = shapeRepository.findAlByShape(name, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return PageResponse.<ShapeDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(shapeMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ShapeDTO> getShapes(int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = shapeRepository.findAllByStatus(status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return PageResponse.<ShapeDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(shapeMapper::toDto).toList())
                .build();

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteShape(Integer id) {
        var shape = shapeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        shape.setStatus(Status.DELETED);
        shapeRepository.save(shape);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ShapeDTO updateShape(Integer id, ShapeDTO shapeDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Shape shape = shapeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        shapeMapper.update(shapeDTO, shape);
        Destiny destiny = destinyRepository.findById(shapeDTO.getDestiny().getId()).orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        shape.setUpdatedBy(name);
        shape.setUpdatedDate(Instant.now());
        shape.setDestiny(destiny);
        return shapeMapper.toDto(shapeRepository.saveAndFlush(shape));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<ShapeDTO> getAllShapes() {
        Status status = Status.ACTIVE;
        return shapeRepository.findAllByStatus(status).stream().map(shapeMapper::toDto).toList();
    }
}
