package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationAnimalMapper;
import com.fengshuisystem.demo.repository.AnimalCategoryRepository;
import com.fengshuisystem.demo.repository.ConsultationAnimalRepository;
import com.fengshuisystem.demo.repository.ConsultationResultRepository;
import com.fengshuisystem.demo.service.ConsultationAnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationAnimalServiceImpl implements ConsultationAnimalService {

    private final ConsultationAnimalRepository consultationAnimalRepository;
    private final ConsultationResultRepository consultationResultRepository;
    private final AnimalCategoryRepository animalCategoryRepository;
    private final ConsultationAnimalMapper consultationAnimalMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationAnimalDTO createConsultationAnimal(ConsultationAnimalDTO dto, Integer resultId, Integer animalCategoryId) {
        log.info("Bắt đầu tạo ConsultationAnimal cho resultId: {} và animalCategoryId: {}", resultId, animalCategoryId);

        // Kiểm tra sự tồn tại của ConsultationResult
        ConsultationResult consultationResult = consultationResultRepository.findById(resultId)
                .orElseThrow(() -> {
                    String errorMessage = "ConsultationResult không tìm thấy với ID: " + resultId;
                    log.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                });

        if (!consultationResult.getStatus().equals(Request.PENDING)) {
            throw new RuntimeException("Result có Status khác PENDING");
        }

        // Kiểm tra sự tồn tại của AnimalCategory
        AnimalCategory animalCategory = animalCategoryRepository.findById(animalCategoryId)
                .orElseThrow(() -> {
                    String errorMessage = "AnimalCategory không tìm thấy với ID: " + animalCategoryId;
                    log.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                });

        // Kiểm tra sự trùng lặp của cặp (resultId, animalCategoryId)
        Optional<ConsultationAnimal> existingAnimal = consultationAnimalRepository.findByConsultationResultIdAndAnimalCategoryId(resultId, animalCategoryId);
        if (existingAnimal.isPresent()) {
            throw new RuntimeException("Cặp (resultId, animalCategoryId) đã tồn tại. Không được phép trùng lặp.");
        }

        // Tạo mới ConsultationAnimal từ DTO
        ConsultationAnimal consultationAnimal = consultationAnimalMapper.toEntity(dto);
        consultationAnimal.setConsultationResult(consultationResult);
        consultationAnimal.setAnimalCategory(animalCategory);
        consultationAnimal.setStatus(Request.COMPLETED);

        // Lưu vào cơ sở dữ liệu
        ConsultationAnimal savedAnimal = consultationAnimalRepository.save(consultationAnimal);
        log.info("Đã tạo ConsultationAnimal với ID: {}", savedAnimal.getId());

        // Trả về DTO sau khi lưu
        return consultationAnimalMapper.toDto(savedAnimal);
    }

}
