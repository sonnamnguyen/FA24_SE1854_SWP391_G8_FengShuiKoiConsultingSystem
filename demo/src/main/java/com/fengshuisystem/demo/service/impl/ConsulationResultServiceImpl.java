package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.constant.PredefinedConsulationCategory;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsulationResultMapper;
import com.fengshuisystem.demo.repository.ConsulationCategoryRepository;
import com.fengshuisystem.demo.repository.ConsulationResultRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsulationResultServiceImpl {
    ConsulationResultRepository consulationResultRepository;
    ConsulationResultMapper consulationResultMapper;
    ConsulationCategoryRepository consulationCategoryRepository;


    public ConsultationResultDTO createConsulationResult(Integer id) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        var consulationResult = consulationResultRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));
        consulationResult.setStatus(Status.COMPLETED);
        consulationResult.setCreatedBy(name);
        consulationResult.setUpdatetedBy(name);
        consulationResult.setCreatedDate(Instant.now());
        consulationResult.setUpdatetedDate(Instant.now());
        if(consulationResult.getConsultationAnimals() != null && consulationResult.getConsultationShelters() == null) {
            var consulationCategory = consulationCategoryRepository.findById(PredefinedConsulationCategory.FISH_CATEGORY).orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));
            consulationResult.setConsultationCategory(consulationCategory);
        } else if(consulationResult.getConsultationShelters() != null && consulationResult.getConsultationAnimals() == null) {
            var consulationCategory = consulationCategoryRepository.findById(PredefinedConsulationCategory.SHELTER_CATEGORY).orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));
            consulationResult.setConsultationCategory(consulationCategory);
        } else if(consulationResult.getConsultationAnimals() != null && consulationResult.getConsultationShelters() != null) {
            var consulationCategory = consulationCategoryRepository.findById(PredefinedConsulationCategory.BOTH_CATEGORY).orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));
            consulationResult.setConsultationCategory(consulationCategory);
        }  else {
            AppException exception = new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED);
            exception.setErrorCode(ErrorCode.CONSULATION_RESULT_NOT_EXISTED);
        }
        return consulationResultMapper.toDto(consulationResult);
    }

    public PageResponse<ConsultationResultDTO> getConsulationBySearch(int page, int size) {
        Status status = Status.COMPLETED;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = consulationResultRepository.findAllByStatus(status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED);
        }
        return PageResponse.<ConsultationResultDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(consulationResultMapper::toDto).toList())
                .build();
    }

    public void deleteConsulationResult(Integer id) {
        var consulationResult = consulationResultRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));
        consulationResult.setStatus(Status.DELETED);
        consulationResultRepository.save(consulationResult);
    }


}
