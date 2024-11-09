package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationShelterMapper;
import com.fengshuisystem.demo.repository.ConsultationResultRepository;
import com.fengshuisystem.demo.repository.ConsultationShelterRepository;
import com.fengshuisystem.demo.repository.ShelterCategoryRepository;
import com.fengshuisystem.demo.service.ConsultationShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationShelterServiceImpl implements ConsultationShelterService {

    private final ConsultationShelterRepository consultationShelterRepository;
    private final ConsultationResultRepository consultationResultRepository;
    private final ShelterCategoryRepository shelterCategoryRepository;
    private final ConsultationShelterMapper consultationShelterMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationShelterDTO createConsultationShelter(ConsultationShelterDTO dto) {
        int resultId = dto.getConsultationResultId();
        int shelterCategoryId = dto.getShelterCategoryId();
        // Kiểm tra xem cặp resultId và shelterCategoryId có tồn tại hay không
        boolean exists = consultationShelterRepository.existsByConsultationResultIdAndShelterCategoryId(resultId, shelterCategoryId);
        if (exists) {
            throw new RuntimeException("Cặp resultId và shelterCategoryId đã tồn tại.");
        }

        // Lấy ConsultationResult dựa trên resultId
        ConsultationResult consultationResult = consultationResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("ConsultationResult không tìm thấy với ID: " + resultId));

        if (!consultationResult.getStatus().equals(Request.PENDING)) {
            throw new RuntimeException("Result có Status khác PENDING");
        }

        // Lấy ShelterCategory dựa trên shelterCategoryId
        ShelterCategory shelterCategory = shelterCategoryRepository.findById(shelterCategoryId)
                .orElseThrow(() -> new RuntimeException("ShelterCategory không tìm thấy với ID: " + shelterCategoryId));

        // Tạo ConsultationShelter từ DTO và thiết lập các quan hệ
        ConsultationShelter shelter = consultationShelterMapper.toEntity(dto);
        shelter.setConsultationResult(consultationResult);
        shelter.setShelterCategory(shelterCategory);
        shelter.setStatus(Request.COMPLETED);

        // Lưu vào database và trả về DTO
        ConsultationShelter savedShelter = consultationShelterRepository.save(shelter);
        return consultationShelterMapper.toDto(savedShelter);
    }

    @Override
    public PageResponse<ConsultationShelterDTO> getAllConsultationShelterPage(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = consultationShelterRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_SHELTER_DOES_NOT_EXIST);
        }
        return PageResponse.<ConsultationShelterDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(consultationShelterMapper::toDto).toList())
                .build();
    }

    @Override
    public ConsultationShelterDTO updateConsultationShelter(Integer id, ConsultationShelterDTO consultationShelterDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        ConsultationShelter consultationShelter = consultationShelterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SHELTER_DOES_NOT_EXIST));

        // Update specific fields only if they are provided in DTO
        if (consultationShelterDTO.getDescription() != null) {
            consultationShelter.setDescription(consultationShelterDTO.getDescription());
        }
        if (consultationShelterDTO.getStatus() != null) {
            consultationShelter.setStatus(consultationShelterDTO.getStatus());
        }

        consultationShelter.setUpdatedBy(name);
        consultationShelter.setUpdatedDate(Instant.now());

        // Save the updated entity and return the DTO
        ConsultationShelter savedResult = consultationShelterRepository.saveAndFlush(consultationShelter);
        return consultationShelterMapper.toDto(savedResult);
    }

    @Override
    public ConsultationShelterDTO getConsultationShelterById(Integer id) {
        return consultationShelterMapper.toDto(consultationShelterRepository.findById(id).orElseThrow());
    }

    @Override
    public List<ConsultationShelterDTO> getAllConsultationShelter() {
        List<ConsultationShelterDTO> consultationShelterDTOS= consultationShelterRepository.findAll()
                .stream()
                .map(consultationShelterMapper::toDto)
                .toList();
        if (consultationShelterDTOS.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_SHELTER_DOES_NOT_EXIST);
        }
        return consultationShelterDTOS;
    }

}
