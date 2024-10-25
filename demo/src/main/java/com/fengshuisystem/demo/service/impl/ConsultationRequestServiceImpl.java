package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationRequestMapper;
import com.fengshuisystem.demo.repository.ConsultationRequestRepository;
import com.fengshuisystem.demo.repository.PackageRepository;
import com.fengshuisystem.demo.repository.UserRepository;
import com.fengshuisystem.demo.service.ConsultationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationRequestServiceImpl implements ConsultationRequestService {

    private final ConsultationRequestRepository consultationRequestRepository;
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final ConsultationRequestMapper consultationRequestMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    public ConsultationRequestDTO createConsultationRequest(ConsultationRequestDTO requestDTO) {
        // 1. Lấy email từ JWT principal
        String email = getCurrentUserEmailFromJwt();
        log.info("Fetched email from JWT: {}", email);

        // 2. Lấy tài khoản từ UserRepository bằng email
        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());

        // 3. Lấy gói dịch vụ từ PackageRepository
        Package packageEntity = packageRepository.findById(requestDTO.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found for ID: " + requestDTO.getPackageId()));
        log.info("Package found: {}", packageEntity.getPackageName());

        // 4. Ánh xạ từ DTO sang Entity và gán tài khoản + gói dịch vụ
        ConsultationRequest consultationRequest = consultationRequestMapper.toEntity(requestDTO);
        consultationRequest.setAccount(account);
        consultationRequest.setPackageId(packageEntity);
        consultationRequest.setStatus(Request.COMPLETED);

        // 5. Lưu vào database
        ConsultationRequest savedRequest = consultationRequestRepository.save(consultationRequest);
        log.info("Consultation request saved with ID: {}", savedRequest.getId());

        // 6. Trả về DTO sau khi lưu thành công
        return consultationRequestMapper.toDTO(savedRequest);
    }

    // Phương thức lấy email từ JWT
    private String getCurrentUserEmailFromJwt() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("sub");  // Lấy email từ claim "email"
        log.info("Extracted email from JWT: {}", email);
        return email;
    }
}
