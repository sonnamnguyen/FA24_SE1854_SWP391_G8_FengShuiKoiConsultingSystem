package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationAnimalMapper;
import com.fengshuisystem.demo.repository.AnimalCategoryRepository;
import com.fengshuisystem.demo.repository.ConsultationAnimalRepository;
import com.fengshuisystem.demo.repository.ConsultationResultRepository;
import com.fengshuisystem.demo.service.ConsultationAnimalService;
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
public class ConsultationAnimalServiceImpl implements ConsultationAnimalService {

    private final ConsultationAnimalRepository consultationAnimalRepository;
    private final ConsultationResultRepository consultationResultRepository;
    private final AnimalCategoryRepository animalCategoryRepository;
    private final ConsultationAnimalMapper consultationAnimalMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationAnimalDTO createConsultationAnimal(ConsultationAnimalDTO dto) {
        int resultId = dto.getConsultationResultId();
        int animalCategoryId = dto.getAnimalCategoryId();

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

        // Lấy danh sách các AnimalCategoryId từ requestDetailId
        Set<AnimalCategory> allowedAnimalCategories = consultationResult.getRequestDetail().getAnimalCategories();

        // Chuyển đổi Set<AnimalCategory> thành Set<Integer> chứa các AnimalCategoryId
        Set<Integer> allowedAnimalCategoryIds = allowedAnimalCategories.stream()
                .map(AnimalCategory::getId)
                .collect(Collectors.toSet());

        // Kiểm tra nếu animalCategoryId không nằm trong danh sách allowedAnimalCategoryIds
        if (!allowedAnimalCategoryIds.contains(animalCategoryId)) {
            throw new RuntimeException("AnimalCategoryId không hợp lệ vì ko tồn tại trong Request");
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

    @Override
    public PageResponse<ConsultationAnimalDTO> getAllConsultationAnimalPage(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = consultationAnimalRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_ANIMAL_DOES_NOT_EXIST);
        }
        return PageResponse.<ConsultationAnimalDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(consultationAnimalMapper::toDto).toList())
                .build();
    }

    @Override
    public ConsultationAnimalDTO updateConsultationAnimal(Integer id, ConsultationAnimalDTO consultationAnimalDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        ConsultationAnimal consultationAnimal = consultationAnimalRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SHELTER_DOES_NOT_EXIST));

        // Update specific fields only if they are provided in DTO
        if (consultationAnimalDTO.getDescription() != null) {
            consultationAnimal.setDescription(consultationAnimalDTO.getDescription());
        }
        if (consultationAnimalDTO.getStatus() != null) {
            consultationAnimal.setStatus(consultationAnimalDTO.getStatus());
        }

        consultationAnimal.setUpdatedBy(name);
        consultationAnimal.setUpdatedDate(Instant.now());

        // Save the updated entity and return the DTO
        ConsultationAnimal savedResult = consultationAnimalRepository.saveAndFlush(consultationAnimal);
        return consultationAnimalMapper.toDto(savedResult);
    }

    @Override
    public ConsultationAnimalDTO getConsultationAnimalById(Integer id) {
        return consultationAnimalMapper.toDto(consultationAnimalRepository.findById(id).orElseThrow());
    }

    @Override
    public List<ConsultationAnimalDTO> getAllConsultationAnimals() {
        List<ConsultationAnimalDTO> consultationAnimalDTOS= consultationAnimalRepository.findAll()
                .stream()
                .map(consultationAnimalMapper::toDto)
                .toList();
        if (consultationAnimalDTOS.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_ANIMAL_DOES_NOT_EXIST);
        }
        return consultationAnimalDTOS;
    }

    @Override
    public List<ConsultationAnimalDTO> searchByResultId(Integer resultId) {
        // Thực hiện truy vấn trong repository để tìm các ConsultationAnimal theo resultId
        List<ConsultationAnimal> animals = consultationAnimalRepository.findByConsultationResultId(resultId);

        // Kiểm tra nếu không tìm thấy kết quả nào
        if (animals.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_ANIMAL_DOES_NOT_EXIST);
        }

        // Chuyển đổi kết quả sang DTO và trả về
        return animals.stream()
                .map(consultationAnimalMapper::toDto)
                .collect(Collectors.toList());
    }

}
