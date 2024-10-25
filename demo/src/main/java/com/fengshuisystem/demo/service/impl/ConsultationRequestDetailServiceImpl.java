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

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationRequestDetailServiceImpl implements ConsultationRequestDetailService {

    private final ConsultationRequestDetailRepository detailRepository;
    private final ConsultationRequestRepository requestRepository;
    private final ConsultationRequestDetailMapper detailMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    public ConsultationRequestDetailDTO getRequestDetailById(Integer requestDetailId) {
        ConsultationRequestDetail detail = detailRepository.findById(requestDetailId)
                .orElseThrow(() -> new RuntimeException("Request detail not found for ID: " + requestDetailId));
        log.info("ConsultationRequestDetail found for ID: {}", requestDetailId);
        return detailMapper.toDTO(detail);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ConsultationRequestDetailDTO createOrUpdateDetail(ConsultationRequestDetailDTO detailDTO, Integer packageId) {
        // Kiểm tra Package có tồn tại và có trạng thái COMPLETED không
        ConsultationRequest consultationRequest = requestRepository.findById(packageId)
                .filter(request -> request.getStatus() == Request.COMPLETED)
                .orElseThrow(() -> new RuntimeException("Package không tìm thấy hoặc chưa hoàn thành, ID: " + packageId));

        ConsultationRequestDetail detail;

        if (detailDTO.getId() != null) {
            // Nếu có ID, tìm và cập nhật chi tiết yêu cầu
            detail = detailRepository.findById(detailDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Chi tiết yêu cầu không tìm thấy với ID: " + detailDTO.getId()));
            detailMapper.updateEntityFromDTO(detailDTO, detail);
        } else {
            // Nếu không có ID, tạo mới chi tiết yêu cầu
            detail = detailMapper.toEntity(detailDTO);
            detail.setConsultationRequest(consultationRequest);
        }

        // Xử lý danh sách animalCategoryIds và shelterCategoryIds từ DTO
        detail.getAnimalCategories().clear(); // Xóa danh mục cũ nếu có
        detailDTO.getAnimalCategoryIds().forEach(animalCategoryId -> {
            AnimalCategory animalCategory = new AnimalCategory();  // Tạo đối tượng AnimalCategory
            animalCategory.setId(animalCategoryId); // Gán ID
            detail.getAnimalCategories().add(animalCategory); // Thêm vào Set
        });

        detail.getShelterCategories().clear(); // Xóa danh mục cũ nếu có
        detailDTO.getShelterCategoryIds().forEach(shelterCategoryId -> {
            ShelterCategory shelterCategory = new ShelterCategory();  // Tạo đối tượng ShelterCategory
            shelterCategory.setId(shelterCategoryId); // Gán ID
            detail.getShelterCategories().add(shelterCategory); // Thêm vào Set
        });

        // Lưu chi tiết yêu cầu
        ConsultationRequestDetail savedDetail = detailRepository.save(detail);
        log.info("Đã lưu ConsultationRequestDetail với ID: {}", savedDetail.getId());

        return detailMapper.toDTO(savedDetail);
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public void cancelRequestDetailById(Integer requestDetailId) {
        ConsultationRequestDetail detail = detailRepository.findById(requestDetailId)
                .orElseThrow(() -> new RuntimeException("Request detail not found for ID: " + requestDetailId));

        detail.setStatus(Request.CANCELLED);  // Cập nhật trạng thái thành CANCELLED
        detailRepository.save(detail);  // Lưu lại thay đổi
        log.info("Cancelled ConsultationRequestDetail with ID: {}", requestDetailId);
    }
}
