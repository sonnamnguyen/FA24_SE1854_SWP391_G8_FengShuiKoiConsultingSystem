package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationRequestDetailMapper;
import com.fengshuisystem.demo.repository.ConsultationRequestDetailRepository;
import com.fengshuisystem.demo.repository.ConsultationRequestRepository;
import com.fengshuisystem.demo.service.ConsultationRequestDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationRequestDetailServiceImpl implements ConsultationRequestDetailService {

    private final ConsultationRequestDetailRepository detailRepository;
    private final ConsultationRequestRepository requestRepository;
    private final ConsultationRequestDetailMapper detailMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ConsultationRequestDetailDTO createConsultationRequestDetail(ConsultationRequestDetailDTO detailDTO, Integer requestId) {
        // Kiểm tra xem ConsultationRequest có tồn tại không
        ConsultationRequest consultationRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    String errorMessage = "ConsultationRequest không tìm thấy với ID: " + requestId;
                    log.error(errorMessage); // Ghi log lỗi
                    throw new RuntimeException(errorMessage);
                });

        // Kiểm tra xem chi tiết yêu cầu đã tồn tại cho requestId chưa
        if (detailRepository.existsByConsultationRequestId(requestId)) {
            String errorMessage = "Chi tiết yêu cầu đã tồn tại cho ConsultationRequest ID: " + requestId;
            log.error(errorMessage); // Ghi log lỗi
            throw new RuntimeException(errorMessage); // Gửi phản hồi lỗi cho FE
        }

        // Khởi tạo đối tượng detail (mới hoặc đã tồn tại)
        ConsultationRequestDetail detail = detailDTO.getId() != null
                ? updateExistingDetail(detailDTO)
                : createNewDetail(detailDTO, consultationRequest);

        // Xử lý danh mục động vật và nơi trú ẩn từ DTO
        updateCategories(detail, detailDTO);

        detail.setStatus(Request.PENDING);

        // Lưu chi tiết yêu cầu
        ConsultationRequestDetail savedDetail = detailRepository.save(detail);
        log.info("Đã lưu ConsultationRequestDetail với ID: {}", savedDetail.getId());

        return detailMapper.toDTO(savedDetail);
    }

    @Override
    public ConsultationRequestDetailDTO findById(Integer id) {
        return detailMapper.toDTO(detailRepository.findById(id).orElse(null));
    }

    @Override
    public PageResponse<ConsultationRequestDetailDTO> getAllConsultationRequestDetail(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = detailRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.CONSULTATION_REQUEST_DETAIL_NOT_FOUND);
        }
        return PageResponse.<ConsultationRequestDetailDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(detailMapper::toDTO).toList())
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ConsultationRequestDetailDTO updateConsultationRequestDetail(Integer id, ConsultationRequestDetailDTO consultationRequestDetailDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        ConsultationRequestDetail consultationRequestDetail = detailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_REQUEST_DETAIL_NOT_FOUND));

        // Update specific fields only if they are provided in DTO
        if (consultationRequestDetailDTO.getDescription() != null) {
            consultationRequestDetail.setDescription(consultationRequestDetailDTO.getDescription());
        }
        if (consultationRequestDetailDTO.getStatus() != null) {
            consultationRequestDetail.setStatus(consultationRequestDetailDTO.getStatus());
        }

        consultationRequestDetail.setUpdatedBy(name);
        consultationRequestDetail.setUpdatedDate(Instant.now());

        // Save the updated entity and return the DTO
        ConsultationRequestDetail savedResult = detailRepository.saveAndFlush(consultationRequestDetail);
        return detailMapper.toDTO(savedResult);
    }

    /**
     * Cập nhật một chi tiết yêu cầu đã tồn tại.
     */
    private ConsultationRequestDetail updateExistingDetail(ConsultationRequestDetailDTO detailDTO) {
        return detailRepository.findById(detailDTO.getId())
                .map(existingDetail -> {
                    detailMapper.updateEntityFromDTO(detailDTO, existingDetail);
                    return existingDetail;
                })
                .orElseThrow(() -> new RuntimeException("Chi tiết yêu cầu không tìm thấy với ID: " + detailDTO.getId()));
    }

    /**
     * Tạo mới một chi tiết yêu cầu.
     */
    private ConsultationRequestDetail createNewDetail(ConsultationRequestDetailDTO detailDTO, ConsultationRequest consultationRequest) {
        ConsultationRequestDetail newDetail = detailMapper.toEntity(detailDTO);
        newDetail.setConsultationRequest(consultationRequest);
        return newDetail;
    }

    /**
     * Cập nhật danh mục động vật và nơi trú ẩn từ DTO.
     */
    private void updateCategories(ConsultationRequestDetail detail, ConsultationRequestDetailDTO detailDTO) {
        detail.setAnimalCategories(
                detailDTO.getAnimalCategoryIds().stream()
                        .map(id -> {
                            AnimalCategory category = new AnimalCategory();
                            category.setId(id);
                            return category;
                        })
                        .collect(Collectors.toSet())
        );

        detail.setShelterCategories(
                detailDTO.getShelterCategoryIds().stream()
                        .map(id -> {
                            ShelterCategory category = new ShelterCategory();
                            category.setId(id);
                            return category;
                        })
                        .collect(Collectors.toSet())
        );
    }
}
