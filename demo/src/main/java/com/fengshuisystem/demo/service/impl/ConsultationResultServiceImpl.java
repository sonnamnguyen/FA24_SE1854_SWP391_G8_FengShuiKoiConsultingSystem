package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.entity.ConsultationCategory;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationResultMapper;
import com.fengshuisystem.demo.repository.ConsultationRequestDetailRepository;
import com.fengshuisystem.demo.repository.ConsultationResultRepository;
import com.fengshuisystem.demo.repository.UserRepository;
import com.fengshuisystem.demo.service.ConsultationResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationResultServiceImpl implements ConsultationResultService {

    private final ConsultationResultRepository consultationResultRepository;
    private final ConsultationRequestDetailRepository requestDetailRepository;
    private final ConsultationResultMapper consultationResultMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ConsultationResultDTO createConsultationResult(Integer consultationRequestId, ConsultationResultDTO dto) {
        // 1. Lấy ConsultationRequestDetail từ consultationRequestId
        ConsultationRequestDetail requestDetail = requestDetailRepository
                .findByConsultationRequestId(consultationRequestId)
                .orElseThrow(() -> new RuntimeException("Request Detail not found for ID: " + consultationRequestId));

        // 2. Lấy ConsultationRequest từ requestDetail
        ConsultationRequest request = requestDetail.getConsultationRequest();

        // 3. Tạo ConsultationCategory từ ID trong DTO
        ConsultationCategory category = new ConsultationCategory();
        category.setId(dto.getConsultationCategoryId());

        // 4. Khởi tạo đối tượng ConsultationResult
        ConsultationResult consultationResult = consultationResultMapper.toEntity(dto);

        consultationResult.setRequestDetail(requestDetail);
        consultationResult.setRequest(request);
        consultationResult.setConsultationCategory(category);

        // 5. Thiết lập thông tin thời gian và người dùng
        consultationResult.setConsultationDate(Instant.now());
        consultationResult.setStatus(Request.PENDING);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        consultationResult.setCreatedBy(username);
        consultationResult.setUpdatedBy(username);
        consultationResult.setCreatedDate(Instant.now());
        consultationResult.setUpdatedDate(Instant.now());

        // 6. Lưu ConsultationResult vào database
        ConsultationResult savedResult = consultationResultRepository.save(consultationResult);

        // 7. Trả về DTO
        return consultationResultMapper.toDto(savedResult);
    }


}


