package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationResultMapper;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.ConsultationResultService;
import com.fengshuisystem.demo.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationResultServiceImpl implements ConsultationResultService {

    private final ConsultationResultRepository consultationResultRepository;
    private final ConsultationResultMapper consultationResultMapper;
    private final ConsultationAnimalRepository consultationAnimalRepository;
    private final ConsultationShelterRepository consultationShelterRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final ConsultationCategoryRepository consultationCategoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public ConsultationResultDTO createConsultationResult(Integer requestId, ConsultationResultDTO dto) {
        // Lấy email từ admin
        String email = getCurrentUserEmailFromJwt();
        log.info("Fetched email from JWT: {}", email);

        // Tìm kiếm tài khoản từ email
        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());

        // Lấy ConsultationRequest bằng ID
        ConsultationRequest request = consultationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("ConsultationRequest not found for ID: " + requestId));

        // Lấy ConsultationRequestDetail từ request (giả định mỗi request chỉ có một detail)
        ConsultationRequestDetail requestDetail = request.getConsultationRequestDetails()
                .stream().findFirst() // lấy detail đầu tiên (1 req - 1 detail)
                .orElseThrow(() -> new RuntimeException("No ConsultationRequestDetail found for request ID: " + requestId));

        // Kiểm tra trạng thái của ConsultationRequest
        if (request.getStatus() != Request.COMPLETED) {
            throw new IllegalStateException("ConsultationRequest is not 'COMPLETED'. Cannot create result.");
        }

        // Kiểm tra trạng thái của ConsultationRequestDetail
        if (requestDetail.getStatus() != Request.PENDING) {
            throw new IllegalStateException("ConsultationRequestDetail is not 'PENDING'. Cannot create result.");
        }

        // Lấy ConsultationCategory từ ID trong DTO (giả định categoryRepository tồn tại)
        ConsultationCategory category = consultationCategoryRepository.findById(dto.getConsultationCategoryId())
                .orElseThrow(() -> new RuntimeException("ConsultationCategory not found for ID: " + dto.getConsultationCategoryId()));

        // Khởi tạo đối tượng ConsultationResult
        ConsultationResult consultationResult = consultationResultMapper.toEntity(dto);
        consultationResult.setAccount(request.getAccount());
        consultationResult.setRequestDetail(requestDetail);
        consultationResult.setRequest(request);
        consultationResult.setConsultationCategory(category);

        // Thiết lập thông tin thời gian và người dùng
        consultationResult.setConsultationDate(Instant.now());

        consultationResult.setStatus(Request.PENDING);

        consultationResult.setConsultantName(dto.getConsultantName());
        consultationResult.setCreatedBy(account.getFullName());
        consultationResult.setUpdatedBy(account.getFullName());
        consultationResult.setCreatedDate(Instant.now());
        consultationResult.setUpdatedDate(Instant.now());

        // Lưu ConsultationResult vào database
        ConsultationResult savedResult = consultationResultRepository.save(consultationResult);

        // Trả về DTO
        return consultationResultMapper.toDto(savedResult);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ConsultationResultDTO updateConsultationResult(Integer resultId) {
        // 1. Lấy ConsultationResult từ resultId
        ConsultationResult consultationResult = consultationResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("ConsultationResult không tìm thấy với ID: " + resultId));

        // 2. Kiểm tra trạng thái ban đầu, nếu không phải PENDING thì không cập nhật
        if (consultationResult.getStatus() != Request.PENDING) {
            throw new RuntimeException("ConsultationResult không ở trạng thái PENDING.");
        }

        // 3. Cập nhật trạng thái thành COMPLETED
        consultationResult.setStatus(Request.COMPLETED);
        consultationResultRepository.save(consultationResult);

        // 4. Lấy email từ account liên quan đến request
        String email = consultationResult.getRequest().getAccount().getEmail();

        // 5. Gửi email với nội dung chi tiết
        sendConsultationDetailsEmail(email, consultationResult);

        // 6. Trả về DTO sau khi cập nhật
        return consultationResultMapper.toDto(consultationResult);
    }

    /**
     * Gửi email với nội dung chi tiết của consultation result, bao gồm các Animal và Shelter.
     */
    private void sendConsultationDetailsEmail(String email, ConsultationResult consultationResult) {
        String subject = "Consultation Result Details - FengShuiConsultingSystem.com";

        // 1. Tạo nội dung email với description chung
        StringBuilder text = new StringBuilder("<html><body>");
        text.append("Dear User,<br/><br/>");
        text.append("Here are the details for your consultation result:<br/><br/>");
        text.append("<strong>Description:</strong> ").append(consultationResult.getDescription()).append("<br/><br/>");

        // 2. Thêm thông tin về từng Animal trong kết quả
        List<ConsultationAnimal> animals = consultationAnimalRepository.findByConsultationResult(consultationResult);
        if (!animals.isEmpty()) {
            text.append("<strong>Animals:</strong><br/>");
            animals.forEach(animal -> {
                text.append("- ").append(animal.getAnimalCategory().getAnimalCategoryName())
                        .append(": ").append(animal.getDescription()).append("<br/>");
            });
            text.append("<br/>");
        }

        // 3. Thêm thông tin về từng Shelter trong kết quả
        List<ConsultationShelter> shelters = consultationShelterRepository.findByConsultationResult(consultationResult);
        if (!shelters.isEmpty()) {
            text.append("<strong>Shelters:</strong><br/>");
            shelters.forEach(shelter -> {
                text.append("- ").append(shelter.getShelterCategory().getShelterCategoryName())
                        .append(": ").append(shelter.getDescription()).append("<br/>");
            });
            text.append("<br/>");
        }

        // 4. Kết thúc email
        text.append("Thank you for using FengShuiConsultingSystem.com!<br/><br/>");
        text.append("Best regards,<br/>The FengShuiConsultingSystem Support Team");
        text.append("</body></html>");

        // 5. Gửi email
        emailService.sendEmail("support@fengshuiconsultingsystem.com", email, subject, text.toString());
    }

    private String getCurrentUserEmailFromJwt() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("sub");
        log.info("Extracted email from JWT: {}", email);
        return email;
    }
}


