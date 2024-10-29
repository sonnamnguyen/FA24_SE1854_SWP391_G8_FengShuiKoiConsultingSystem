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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationResultServiceImpl implements ConsultationResultService {

    private final ConsultationResultRepository consultationResultRepository;
    private final ConsultationRequestDetailRepository requestDetailRepository;
    private final ConsultationResultMapper consultationResultMapper;
    private final ConsultationAnimalRepository consultationAnimalRepository;
    private final ConsultationShelterRepository consultationShelterRepository;
    private final EmailService emailService;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ConsultationResultDTO createConsultationResult(Integer requestId, ConsultationResultDTO dto) {
        // 1. Lấy ConsultationRequestDetail từ consultationRequestId
        ConsultationRequestDetail requestDetail = requestDetailRepository
                .findByConsultationRequestId(requestId)
                .orElseThrow(() -> new RuntimeException("Request Detail not found for ID: " + requestId));

        // 2. Lấy ConsultationRequest từ requestDetail
        ConsultationRequest request = requestDetail.getConsultationRequest();

        // **Kiểm tra trạng thái của ConsultationRequest**
        if (request.getStatus() != Request.COMPLETED) {
            throw new IllegalStateException("ConsultationRequest is not 'COMPLETED'. Cannot create result.");
        }

        // **Kiểm tra trạng thái của ConsultationRequestDetail nếu cần**
        if (requestDetail.getStatus() != Request.COMPLETED) {
            throw new IllegalStateException("Request detail is not 'COMPLETED'. Cannot create result.");
        }

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
        String email = consultationResult.getRequestDetail().getConsultationRequest()
                .getAccount().getEmail();

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
}


