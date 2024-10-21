package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.mapper.ConsultationRequestMapper;
import com.fengshuisystem.demo.repository.ConsultationRequestRepository;
import com.fengshuisystem.demo.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationRequestServiceImpl {

    private final ConsultationRequestRepository requestRepository;
    private final PackageRepository packageRepository;
    private final ConsultationRequestMapper requestMapper;

    public ConsultationRequestDTO createRequest(Integer packageId, ConsultationRequestDTO requestDTO) {
        // Kiểm tra nếu package tồn tại
        Package packageField = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package with ID " + packageId + " not found"));

        // Chuyển DTO sang Entity và thiết lập các trường
        ConsultationRequest request = requestMapper.toEntity(requestDTO);
        request.setPackageField(packageField);
        request.setStatus(Status.COMPLETED);
        request.setCreatedBy("system");
        request.setUpdatetedBy("system");

        // Lưu vào cơ sở dữ liệu
        ConsultationRequest savedRequest = requestRepository.save(request);

        // Chuyển Entity đã lưu về DTO và trả về
        return requestMapper.toDTO(savedRequest);
    }

}
