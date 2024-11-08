package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ConsultationCategoryDTO;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationCategoryMapper;
import com.fengshuisystem.demo.repository.ConsultationCategoryRepository;
import com.fengshuisystem.demo.service.ConsultationCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationCategoryServiceImpl implements ConsultationCategoryService {

    private final ConsultationCategoryMapper consultationCategoryMapper;
    private final ConsultationCategoryRepository consultationCategoryRepository;
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
