package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationRequestDetailMapper;
import com.fengshuisystem.demo.repository.ConsultationRequestDetailRepository;
import com.fengshuisystem.demo.repository.ConsultationRequestRepository;
import com.fengshuisystem.demo.service.ConsultationRequestDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
