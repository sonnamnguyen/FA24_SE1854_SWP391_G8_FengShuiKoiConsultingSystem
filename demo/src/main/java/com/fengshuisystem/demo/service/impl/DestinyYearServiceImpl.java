package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.DestinyYearDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.DestinyYear;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.DestinyYearMapper;
import com.fengshuisystem.demo.repository.DestinyYearRepository;
import com.fengshuisystem.demo.service.DestinyYearService;
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
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DestinyYearServiceImpl implements DestinyYearService {

    private final DestinyYearRepository destinyYearRepository;
    private final DestinyYearMapper destinyYearMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public DestinyYearDTO createDestinyYear(DestinyYearDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        DestinyYear destinyYear = destinyYearMapper.toEntity(request);
        destinyYear.setCreatedBy(name);
        destinyYear.setCreatedDate(Instant.now());
        destinyYear.setStatus(Status.ACTIVE);
        return destinyYearMapper.toDto(destinyYearRepository.save(destinyYear));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<DestinyYearDTO> getDestinyYears(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = destinyYearRepository.findAllByStatus(Status.ACTIVE,pageable);

        if (pageData.isEmpty()) {
            throw new AppException(ErrorCode.DESTINY_YEAR_NOT_EXISTED);
        }

        return PageResponse.<DestinyYearDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(destinyYearMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public DestinyYearDTO updateDestinyYear(Integer id, DestinyYearDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        DestinyYear destinyYear = destinyYearRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_YEAR_NOT_EXISTED));
        destinyYearMapper.update(request, destinyYear);
        destinyYear.setUpdatedBy(name);
        destinyYear.setUpdatedDate(Instant.now());
        return destinyYearMapper.toDto(destinyYearRepository.save(destinyYear));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDestinyYear(Integer id) {
        var destinyYear = destinyYearRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_YEAR_NOT_EXISTED));
        destinyYear.setStatus(Status.DELETED);
        destinyYearRepository.save(destinyYear);
    }
}
