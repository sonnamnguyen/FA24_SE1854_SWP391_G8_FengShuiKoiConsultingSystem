package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.ConsultationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationRequestServiceImpl implements ConsultationRequestService {

    private final ConsultationRequestRepository consultationRequestRepository;
    private final AnimalCategoryRepository animalCategoryRepository;
    private final ShelterCategoryRepository shelterCategoryRepository;
    private final PackageRepository packageRepository;
    private final BillRepository billRepository;

    @Transactional
    @Override
    public ApiResponse<String> createConsultationRequest(ConsultationRequestDTO requestDTO) {
        // Lấy package từ ID
        Package packageEntity = packageRepository.findById(requestDTO.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Instant now = Instant.now();

        // Tạo request từ DTO và gán các thông tin cần thiết
        ConsultationRequest request = ConsultationRequest.builder()
                .account(new Account(requestDTO.getAccount().getUsername(), requestDTO.getAccount().getDob()))
                .packageField(packageEntity)
                .description(requestDTO.getDescription())
                .status(Status.ACTIVE) // Hoặc trạng thái mặc định của bạn
                .createdDate(now)
                .createdBy(currentUser)
                .updatetedDate(now)
                .updatetedBy(currentUser)
                .consultationRequestDetails(createDetails(requestDTO))
                .build();

        // Lưu request và tính tổng giá
        ConsultationRequest savedRequest = consultationRequestRepository.save(request);
        double totalAmount = savedRequest.getConsultationRequestDetails()
                .stream().mapToDouble(ConsultationRequestDetail::getPrice).sum();

        // Trả về API response
        return ApiResponse.<String>builder()
                .result("Consultation request created successfully")
                .build();
    }

    private Set<ConsultationRequestDetail> createDetails(ConsultationRequestDTO requestDTO) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName(); // Lấy tên người dùng hiện tại
        Instant now = Instant.now(); // Thời gian hiện tại

        return requestDTO.getConsultationRequestDetails().stream()
                .map(detailDTO -> {
                    ConsultationRequestDetail detail = new ConsultationRequestDetail();

                    if (detailDTO.getAnimalCategoryId() != null) {
                        AnimalCategory animal = animalCategoryRepository.findById(detailDTO.getAnimalCategoryId())
                                .orElseThrow(() -> new RuntimeException("Animal Category not found"));
                        detail.setAnimalCategory(animal);
                    }

                    if (detailDTO.getShelterCategoryId() != null) {
                        ShelterCategory shelter = shelterCategoryRepository.findById(detailDTO.getShelterCategoryId())
                                .orElseThrow(() -> new RuntimeException("Shelter Category not found"));
                        detail.setShelterCategory(shelter);
                    }

                    // Gán các thông tin bắt buộc
                    detail.setPrice(detailDTO.getPrice());
                    detail.setDescription(detailDTO.getDescription());
                    detail.setCreatedBy(currentUser); // Gán người tạo
                    detail.setUpdatetedBy(currentUser); // Gán người cập nhật
                    detail.setCreatedDate(now); // Gán thời gian tạo
                    detail.setUpdatetedDate(now); // Gán thời gian cập nhật
                    detail.setStatus(Status.ACTIVE); // Trạng thái mặc định

                    return detail;
                }).collect(Collectors.toSet());
    }
}
