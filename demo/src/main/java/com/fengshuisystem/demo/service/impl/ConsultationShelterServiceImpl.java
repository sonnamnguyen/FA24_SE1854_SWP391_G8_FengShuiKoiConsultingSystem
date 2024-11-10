package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.AnimalCategory;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

        log.info("Bắt đầu tạo ConsultationShelter cho resultId: {} và animalCategoryId: {}", resultId, shelterCategoryId);

        // Lấy ConsultationResult dựa trên resultId
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
        ShelterCategory shelterCategory = shelterCategoryRepository.findById(shelterCategoryId)
                .orElseThrow(() -> {
                    String errorMessage = "AnimalCategory không tìm thấy với ID: " + shelterCategoryId;
                    log.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                });

        // Kiểm tra xem cặp resultId và shelterCategoryId có tồn tại hay không
        Optional<ConsultationShelter> existingShelter = consultationShelterRepository.findByConsultationResultIdAndShelterCategoryId(resultId, shelterCategoryId);
        if (existingShelter.isPresent()) {
            throw new RuntimeException("Cặp resultId và shelterCategoryId đã tồn tại.");
        }

        // Lấy danh sách các ShelterCategoryId từ requestDetailId
        Set<ShelterCategory> allowedShelterCategories = consultationResult.getRequestDetail().getShelterCategories();

        // Chuyển đổi Set<ShelterCategory> thành Set<Integer> chứa các ShelterCategoryId
        Set<Integer> allowedShelterCategoryIds = allowedShelterCategories.stream()
                .map(ShelterCategory::getId)
                .collect(Collectors.toSet());

        // Kiểm tra nếu shelterCategoryId không nằm trong danh sách allowedAnimalCategoryIds
        if (!allowedShelterCategoryIds.contains(shelterCategoryId)) {
            throw new RuntimeException("ShelterCategoryId không hợp lệ vì ko tồn tại trong Request");
        }

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
    public List<ConsultationShelterDTO> searchByResultId(Integer resultId) {
        // Thực hiện truy vấn trong repository để tìm các ConsultationShelter theo resultId
        List<ConsultationShelter> shelters = consultationShelterRepository.findByConsultationResultId(resultId);

        // Kiểm tra nếu không tìm thấy kết quả nào
        if (shelters.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_SHELTER_DOES_NOT_EXIST);
        }

        // Chuyển đổi kết quả sang DTO và trả về
        return shelters.stream()
                .map(consultationShelterMapper::toDto)
                .collect(Collectors.toList());
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
