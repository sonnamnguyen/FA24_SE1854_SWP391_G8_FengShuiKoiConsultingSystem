package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationRequestMapper;
import com.fengshuisystem.demo.repository.BillRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationRequestServiceImpl implements ConsultationRequestService {

    private final ConsultationRequestRepository consultationRequestRepository;
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final ConsultationRequestMapper consultationRequestMapper;
    private final BillRepository billRepository;

    @Override
    @PreAuthorize("hasRole('USER')")
    public ConsultationRequestDTO createConsultationRequest(ConsultationRequestDTO requestDTO) {
        String email = getCurrentUserEmailFromJwt();
        log.info("Fetched email from JWT: {}", email);

        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());

        Package packageEntity = packageRepository.findById(requestDTO.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found for ID: " + requestDTO.getPackageId()));
        log.info("Package found: {}", packageEntity.getPackageName());

        ConsultationRequest consultationRequest = consultationRequestMapper.toEntity(requestDTO);
        consultationRequest.setAccount(account);
        consultationRequest.setPackageId(packageEntity);
        consultationRequest.setStatus(Request.PENDING);
        consultationRequest.setFullName(requestDTO.getFullName());
        consultationRequest.setGender(requestDTO.getGender()); // combo box
        consultationRequest.setEmail(requestDTO.getEmail());
        consultationRequest.setPhone(requestDTO.getPhone());

        ConsultationRequest savedRequest = consultationRequestRepository.save(consultationRequest);
        log.info("Consultation request saved with ID: {}", savedRequest.getId());

        return consultationRequestMapper.toDTO(savedRequest);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public ConsultationRequestDTO updateStatusConsultationRequest(Integer requestId) {
        String email = getCurrentUserEmailFromJwt();
        log.info("Fetched email from JWT: {}", email);

        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());

        // Tìm ConsultationRequest bằng requestId
        ConsultationRequest consultationRequest = consultationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Consultation Request với ID: " + requestId));

        // Cập nhật trạng thái của ConsultationRequest
        if (consultationRequest.getStatus().equals(Request.PENDING)) {
            consultationRequest.setStatus(Request.COMPLETED);
        } else {
            throw new RuntimeException("Request này đã hoàn thành hoặc bị hủy bỏ. Không thể update trạng thái!!!");
        }

        consultationRequest = consultationRequestRepository.save(consultationRequest);
        log.info("Consultation Request ID: {} đã được cập nhật thành COMPLETED.", requestId);

        // Tìm danh sách Bill liên quan đến ConsultationRequest
        List<Bill> bills = billRepository.findAllByConsultationRequestId(requestId);
        if (bills.isEmpty()) {
            throw new RuntimeException("Không tìm thấy Bill cho Consultation Request ID: " + requestId);
        }

        // Cập nhật trạng thái của tất cả các Bill trong danh sách
        for (Bill bill : bills) {
            if (bill.getStatus().equals(BillStatus.PAID)) {
                throw new RuntimeException("Tồn tại Bill đã hủy bỏ hoặc thanh toán. Không thể tiến hành update!!!");
            }
        }

        for (Bill bill : bills) {
            bill.setStatus(BillStatus.PAID);

            billRepository.save(bill);
            log.info("Bill ID: {} liên quan đến Consultation Request ID: {} đã được cập nhật thành PAID.", bill.getId(), requestId);
        }

        // Trả về DTO của ConsultationRequest đã cập nhật
        return consultationRequestMapper.toDTO(consultationRequest);
    }

    private String getCurrentUserEmailFromJwt() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("sub");
        log.info("Extracted email from JWT: {}", email);
        return email;
    }
}
