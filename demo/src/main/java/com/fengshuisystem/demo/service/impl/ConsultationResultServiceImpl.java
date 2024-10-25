package com.fengshuisystem.demo.service.impl;


import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.mapper.ConsultationResultMapper;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.ConsultationResultService;
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
    private final ConsultationRequestDetailRepository requestDetailRepository;
    private final ConsultationResultMapper consultationResultMapper;
    private final UserRepository userRepository;
    private final AnimalCategoryRepository animalCategoryRepository;
    private final ShelterCategoryRepository shelterCategoryRepository;

    @Override
    @PreAuthorize("hasRole('USER')")
    public ConsultationResultDTO createConsultationResult(ConsultationResultDTO resultDTO, Integer consultationRequestDetailId) {
        // 1. Lấy email từ JWT
        String email = getCurrentUserEmailFromJwt();
        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());

        // 2. Kiểm tra trạng thái của ConsultationRequestDetail
        ConsultationRequestDetail requestDetail = requestDetailRepository.findById(consultationRequestDetailId)
                .orElseThrow(() -> new RuntimeException("Consultation Request Detail not found"));

        if (requestDetail.getStatus() != Request.COMPLETED) {
            throw new RuntimeException("Consultation Request Detail is not completed.");
        }

        // 3. Lấy danh sách AnimalCategory từ bảng trung gian
        List<AnimalCategory> selectedAnimals = animalCategoryRepository
                .findByConsultationRequestDetailsId(consultationRequestDetailId);
        log.info("Found {} AnimalCategory IDs for RequestDetail ID: {}", selectedAnimals.size(), consultationRequestDetailId);

        // 4. Lấy danh sách ShelterCategory từ bảng trung gian
        List<ShelterCategory> selectedShelters = shelterCategoryRepository
                .findByConsultationRequestDetailsId(consultationRequestDetailId);
        log.info("Found {} ShelterCategory IDs for RequestDetail ID: {}", selectedShelters.size(), consultationRequestDetailId);

        // 5. Tạo và lưu ConsultationResult
        ConsultationResult consultationResult = consultationResultMapper.toEntity(resultDTO);
        consultationResult.setAccount(account);
        consultationResult.setRequestDetail(requestDetail);
        consultationResult.setConsultationDate(Instant.now());
        consultationResult.setStatus(Request.COMPLETED);

        ConsultationResult savedResult = consultationResultRepository.save(consultationResult);

        // 6. Ánh xạ dữ liệu sang DTO để trả về
        ConsultationResultDTO consultationResultDTO = consultationResultMapper.toDto(savedResult);

        // 7. Gán danh sách AnimalCategory và ShelterCategory vào DTO
        consultationResultDTO.setConsultationAnimals(
                selectedAnimals.stream()
                        .map(animal -> new ConsultationAnimalDTO(
                                animal.getId(),
                                animal.getId(),
                                animal.getDescription(),
                                Request.PENDING,  // Trạng thái tạm thời
                                animal.getCreatedDate(),
                                animal.getCreatedBy(),
                                animal.getUpdatedDate(),
                                animal.getUpdatedBy()
                        ))
                        .toList()
        );

        consultationResultDTO.setConsultationShelters(
                selectedShelters.stream()
                        .map(shelter -> new ConsultationShelterDTO(
                                shelter.getId(),
                                shelter.getId(),
                                shelter.getDescription(),
                                Request.PENDING,  // Trạng thái tạm thời
                                shelter.getCreatedDate(),
                                shelter.getCreatedBy(),
                                shelter.getUpdatedDate(),
                                shelter.getUpdatedBy()
                        ))
                        .toList()
        );

        return consultationResultDTO;
    }

    // Phương thức lấy email từ JWT
    private String getCurrentUserEmailFromJwt() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("sub");
        log.info("Extracted email from JWT: {}", email);
        return email;
    }
}
