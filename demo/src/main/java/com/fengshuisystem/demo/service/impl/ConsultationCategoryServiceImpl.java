package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationCategoryDTO;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationCategoryMapper;
import com.fengshuisystem.demo.repository.ConsultationCategoryRepository;
import com.fengshuisystem.demo.service.ConsultationCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationCategoryServiceImpl implements ConsultationCategoryService {

    ConsultationCategoryMapper consultationCategoryMapper;
    ConsultationCategoryRepository consultationCategoryRepository;

    @Override
    public List<ConsultationCategoryDTO> getAllConsultationCategory() {
        List<ConsultationCategoryDTO> consultationCategoryDTOS = consultationCategoryRepository.findAll()
                .stream()
                .map(consultationCategoryMapper::toDto)
                .toList();
        if (consultationCategoryDTOS.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_CATEGORY_NOT_EXISTED);
        }
        return consultationCategoryDTOS;
    }
}
