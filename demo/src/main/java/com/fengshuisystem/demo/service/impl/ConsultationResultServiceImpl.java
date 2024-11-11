package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationResultMapper;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.ConsultationResultService;
import com.fengshuisystem.demo.service.EmailService;
import com.fengshuisystem.demo.service.NotificationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationResultServiceImpl implements ConsultationResultService {

    private final NotificationService notificationService;

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
        // Kiểm tra các trường bắt buộc trong DTO
        if (dto.getConsultationCategoryId() == null) {
            throw new RuntimeException("Consultation Category is required.");
        }
        if (dto.getConsultantName() == null || dto.getConsultantName().isBlank()) {
            throw new RuntimeException("Consultant Name is required.");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new RuntimeException("Description is required.");
        }
        if (dto.getStatus() == null) {
            throw new RuntimeException("Status is required.");
        }
        if (requestId == null) {
            throw new RuntimeException("Consultation Request is required.");
        }

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
    public ConsultationResultDTO updateConsultationResultAndSendMail(Integer resultId) {
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
        consultationResult.getRequestDetail().setStatus(Request.COMPLETED);

        // 4. Lấy email từ account liên quan đến request
        String email = consultationResult.getRequest().getEmail();
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email của người dùng không có sẵn.");
        }

        // Kiểm tra account liên quan
        if (consultationResult.getAccount() == null) {
            throw new RuntimeException("Account của người dùng không có sẵn.");
        }

        // Lấy danh sách AnimalCategory và ShelterCategory từ request
        Set<AnimalCategory> animalsNeedToConsultation = consultationResult.getRequestDetail().getAnimalCategories();
        Set<ShelterCategory> sheltersNeedToConsultation = consultationResult.getRequestDetail().getShelterCategories();

        // Lọc danh sách ConsultationAnimal và ConsultationShelter khớp với các category từ requestDetail
        List<ConsultationAnimal> matchingAnimals = consultationAnimalRepository.findByConsultationResult(consultationResult)
                .stream()
                .filter(animal -> animalsNeedToConsultation.contains(animal.getAnimalCategory()))
                .collect(Collectors.toList());

        List<ConsultationShelter> matchingShelters = consultationShelterRepository.findByConsultationResult(consultationResult)
                .stream()
                .filter(shelter -> sheltersNeedToConsultation.contains(shelter.getShelterCategory()))
                .collect(Collectors.toList());

        // Kiểm tra trạng thái của các animal và shelter trong matchingAnimals và matchingShelters
        boolean allAnimalsCompleted = matchingAnimals.stream()
                .allMatch(animal -> animal.getStatus() == Request.COMPLETED);
        boolean allSheltersCompleted = matchingShelters.stream()
                .allMatch(shelter -> shelter.getStatus() == Request.COMPLETED);

        if (!allAnimalsCompleted) {
            throw new RuntimeException("Một số ConsultationAnimal chưa ở trạng thái COMPLETED.");
        }
        if (!allSheltersCompleted) {
            throw new RuntimeException("Một số ConsultationShelter chưa ở trạng thái COMPLETED.");
        }

        // Tìm AnimalCategory nào từ requestDetail không có trong matchingAnimals
        Set<Integer> matchedAnimalCategoryIds = matchingAnimals.stream()
                .map(animal -> animal.getAnimalCategory().getId())
                .collect(Collectors.toSet());
        Set<Integer> missingAnimalCategoryIds = animalsNeedToConsultation.stream()
                .map(AnimalCategory::getId)
                .filter(id -> !matchedAnimalCategoryIds.contains(id))
                .collect(Collectors.toSet());

        if (!missingAnimalCategoryIds.isEmpty()) {
            System.out.println("Các AnimalCategory thiếu trong ConsultationAnimal: " + missingAnimalCategoryIds);
            throw new RuntimeException("Thiếu các AnimalCategory ID: " + missingAnimalCategoryIds);
        }

        // Tìm ShelterCategory nào từ requestDetail không có trong matchingShelters
        Set<Integer> matchedShelterCategoryIds = matchingShelters.stream()
                .map(shelter -> shelter.getShelterCategory().getId())
                .collect(Collectors.toSet());
        Set<Integer> missingShelterCategoryIds = sheltersNeedToConsultation.stream()
                .map(ShelterCategory::getId)
                .filter(id -> !matchedShelterCategoryIds.contains(id))
                .collect(Collectors.toSet());

        if (!missingShelterCategoryIds.isEmpty()) {
            System.out.println("Các ShelterCategory thiếu trong ConsultationShelter: " + missingShelterCategoryIds);
            throw new RuntimeException("Thiếu các ShelterCategory ID: " + missingShelterCategoryIds);
        }

        // 5. Gửi email với danh sách chính xác từ requestDetail
        sendConsultationDetailsEmail(email, consultationResult, matchingAnimals, matchingShelters);

        // Kiểm tra thông tin cần thiết cho notification
        NotificationFCM notificationFCM = new NotificationFCM();
        String title = "Result of your request #" + consultationResult.getRequest().getId();
        String message = "The result of request #" + consultationResult.getRequest().getId() + " has been sent to the email: " + email + ". Please check and confirm. If there are any issues, contact our hotline for support and to report any errors.";

        if (title == null || title.isEmpty() || message == null || message.isEmpty()) {
            throw new RuntimeException("Thiếu thông tin tiêu đề hoặc nội dung thông báo.");
        }

        notificationFCM.setTitle(title);
        notificationFCM.setMessage(message);

        // Gửi thông báo
        notificationService.sendNotificationToAccount(notificationFCM, consultationResult.getAccount());

        // 6. Trả về DTO sau khi cập nhật
        return consultationResultMapper.toDto(consultationResult);
    }


    /**
     * Gửi email với nội dung chi tiết của consultation result, bao gồm các Animal và Shelter.
     */
    private void sendConsultationDetailsEmail(String email, ConsultationResult consultationResult, List<ConsultationAnimal> animals, List<ConsultationShelter> shelters) {
        String subject = "Consultation Result Details - FengShuiConsultingSystem.com";


        // 1. Tạo nội dung email với description chung và trang trí
        StringBuilder text = new StringBuilder("<html><body style='font-family: Arial, sans-serif; line-height: 1.6; margin: 20px;'>");
        text.append("<h2 style='color: #2c3e50; margin-bottom: 10px;'>Dear User,</h2>");
        text.append("<p>Here are the details for your consultation result:</p>");
        text.append("<hr style='border: 1px solid #ccc; margin-bottom: 20px;'/>");

        // Làm nổi bật phần mô tả chính
        text.append("<h3 style='color: #2980b9;'>Description</h3>");
        text.append("<div style='background-color: #f2f9ff; padding: 15px; border-left: 5px solid #2980b9; margin-bottom: 20px; font-size: 1.1em;'>")
                .append(consultationResult.getDescription())
                .append("</div>");

        // 2. Thêm thông tin về từng Animal trong kết quả
        if (!animals.isEmpty()) {
            text.append("<h3 style='color: #2980b9;'>Animals</h3>");
            animals.forEach(animal -> {
                text.append("<div style='margin-bottom: 20px; padding: 10px; border: 1px solid #ddd; border-radius: 5px; background-color: #fdfdfd;'>")
                        .append("<h4 style='color: #27ae60;'>").append(animal.getAnimalCategory().getAnimalCategoryName()).append("</h4>")
                        .append("<div style='margin-bottom: 10px; background-color: #e8f4fc; padding: 10px; border-radius: 4px;'>")
                        .append("<p style='margin: 0; font-size: 1.1em; color: #8e44ad;'><strong>Description:</strong> ")
                        .append(animal.getDescription()).append("</p>") // Nổi bật mô tả chính trong khung riêng biệt
                        .append("</div>")
                        .append("<div style='font-size: 0.95em; color: #555;'>")
                        .append("<strong>Category Description:</strong> ").append(animal.getAnimalCategory().getDescription()).append("<br/>")
                        .append("<strong>Origin:</strong> ").append(animal.getAnimalCategory().getOrigin()).append("<br/>")
                        .append("</div>")
                        .append("</div>");
            });
        }

        // 3. Thêm thông tin về từng Shelter trong kết quả
        if (!shelters.isEmpty()) {
            text.append("<h3 style='color: #2980b9;'>Shelters</h3>");
            shelters.forEach(shelter -> {
                text.append("<div style='margin-bottom: 20px; padding: 10px; border: 1px solid #ddd; border-radius: 5px; background-color: #fdfdfd;'>")
                        .append("<h4 style='color: #27ae60;'>").append(shelter.getShelterCategory().getShelterCategoryName()).append("</h4>")
                        .append("<div style='margin-bottom: 10px; background-color: #e8f4fc; padding: 10px; border-radius: 4px;'>")
                        .append("<p style='margin: 0; font-size: 1.1em; color: #8e44ad;'><strong>Description:</strong> ")
                        .append(shelter.getDescription()).append("</p>") // Nổi bật mô tả chính trong khung riêng biệt
                        .append("</div>")
                        .append("<div style='font-size: 0.95em; color: #555;'>")
                        .append("<strong>Category Description:</strong> ").append(shelter.getShelterCategory().getDescription()).append("<br/>")
                        .append("<strong>Diameter:</strong> ").append(shelter.getShelterCategory().getDiameter()).append(" cm<br/>")
                        .append("<strong>Height:</strong> ").append(shelter.getShelterCategory().getHeight()).append(" cm<br/>")
                        .append("<strong>Length:</strong> ").append(shelter.getShelterCategory().getLength()).append(" cm<br/>")
                        .append("<strong>Water Filtration System:</strong> ").append(shelter.getShelterCategory().getWaterFiltrationSystem()).append("<br/>")
                        .append("<strong>Water Volume:</strong> ").append(shelter.getShelterCategory().getWaterVolume()).append(" liters<br/>")
                        .append("</div>")
                        .append("</div>");
            });
        }

        // 4. Kết thúc email với lời cảm ơn
        text.append("<hr style='border: 1px solid #ccc; margin-top: 20px;'/>");
        text.append("<p>Thank you for using FengShuiConsultingSystem.com!</p>");
        text.append("<p>Best regards,<br/><strong>The FengShuiConsultingSystem Support Team</strong></p>");
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

    public PageResponse<ConsultationResultDTO> getAllConsultationResult(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = consultationResultRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED);
        }
        return PageResponse.<ConsultationResultDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(consultationResultMapper::toDto).toList())
                .build();
    }

    public PageResponse<ConsultationResultDTO> getConsultationResultBySearch(String search, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = consultationResultRepository.findAllByConsultantName(search, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED);
        }

        return PageResponse.<ConsultationResultDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(consultationResultMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ConsultationResultDTO updateConsultationResult(Integer id, @Valid ConsultationResultDTO consultationResultDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        // Lấy ConsultationResult từ DB
        ConsultationResult consultationResult = consultationResultRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));

        // Cập nhật các trường từ DTO
        if (consultationResultDTO.getConsultantName() != null) {
            consultationResult.setConsultantName(consultationResultDTO.getConsultantName());
        }
        if (consultationResultDTO.getDescription() != null) {
            consultationResult.setDescription(consultationResultDTO.getDescription());
        }
        if (consultationResultDTO.getStatus() != null) {
            consultationResult.setStatus(consultationResultDTO.getStatus());
        }

        // Cập nhật ConsultationCategory từ consultationCategoryId
        if (consultationResultDTO.getConsultationCategoryId() != null) {
            ConsultationCategory category = consultationCategoryRepository.findById(consultationResultDTO.getConsultationCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_CATEGORY_NOT_EXISTED));
            consultationResult.setConsultationCategory(category);
        }

        consultationResult.setUpdatedBy(name);
        consultationResult.setUpdatedDate(Instant.now());

        // Lưu đối tượng đã cập nhật và trả về DTO
        ConsultationResult savedResult = consultationResultRepository.saveAndFlush(consultationResult);
        return consultationResultMapper.toDto(savedResult);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public long countCompletedConsultations() {
        return consultationResultRepository.countByStatus(Request.COMPLETED);
    }

    @Override
    public List<ConsultationResultDTO> getAll() {
        List<ConsultationResultDTO> consultationResultDTOS= consultationResultRepository.findAll()
                .stream()
                .map(consultationResultMapper::toDto)
                .toList();
        if (consultationResultDTOS.isEmpty()) {
            throw new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED);
        }
        return consultationResultDTOS;
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ConsultationResultDTO> getUserConsultationResults(String email) {
        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());

        List<ConsultationResult> results = consultationResultRepository.findByAccount(account);
        return results.stream().map(consultationResultMapper::toDto).collect(Collectors.toList());
    }
}


