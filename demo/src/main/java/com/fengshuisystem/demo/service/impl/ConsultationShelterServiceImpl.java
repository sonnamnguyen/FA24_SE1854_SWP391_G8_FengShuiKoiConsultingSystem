package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationShelterMapper;
import com.fengshuisystem.demo.repository.ConsultationResultRepository;
import com.fengshuisystem.demo.repository.ConsultationShelterRepository;
import com.fengshuisystem.demo.repository.ShelterCategoryRepository;
import com.fengshuisystem.demo.service.ConsultationShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationShelterServiceImpl implements ConsultationShelterService {

    private final ConsultationShelterRepository consultationShelterRepository;
    private final ConsultationResultRepository consultationResultRepository;
    private final ShelterCategoryRepository shelterCategoryRepository;
    private final ConsultationShelterMapper consultationShelterMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationShelterDTO createConsultationShelter(ConsultationShelterDTO dto, Integer resultId, Integer shelterCategoryId) {
        // Kiểm tra xem cặp resultId và shelterCategoryId có tồn tại hay không
        boolean exists = consultationShelterRepository.existsByConsultationResultIdAndShelterCategoryId(resultId, shelterCategoryId);
        if (exists) {
            throw new RuntimeException("Cặp resultId và shelterCategoryId đã tồn tại.");
        }

        // Lấy ConsultationResult dựa trên resultId
        ConsultationResult consultationResult = consultationResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("ConsultationResult not found with ID: " + resultId));

        // Lấy ShelterCategory dựa trên shelterCategoryId
        ShelterCategory shelterCategory = shelterCategoryRepository.findById(shelterCategoryId)
                .orElseThrow(() -> new RuntimeException("ShelterCategory not found with ID: " + shelterCategoryId));

        // Tạo ConsultationShelter từ DTO và thiết lập các quan hệ
        ConsultationShelter shelter = consultationShelterMapper.toEntity(dto);
        shelter.setConsultationResult(consultationResult);
        shelter.setShelterCategory(shelterCategory);
        shelter.setStatus(Request.COMPLETED);

        // Lưu vào database và trả về DTO
        ConsultationShelter savedShelter = consultationShelterRepository.save(shelter);
        return consultationShelterMapper.toDto(savedShelter);
    }
}
