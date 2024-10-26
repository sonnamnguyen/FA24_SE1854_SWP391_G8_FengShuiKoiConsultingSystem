package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.service.impl.ConsultationShelterServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultation-shelter")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationShelterController {

    ConsultationShelterServiceImpl consultationShelterService;

    @PostMapping("/request-detail/{requestDetailId}/shelter-category/{shelterCategoryId}")
    public ApiResponse<ConsultationShelterDTO> createConsultationShelter(
            @RequestBody ConsultationShelterDTO consultationShelterDTO,
            @PathVariable Integer requestDetailId,
            @PathVariable Integer shelterCategoryId) {
        ConsultationShelterDTO result = consultationShelterService.createConsultationShelter(
                consultationShelterDTO, requestDetailId, shelterCategoryId);
        return ApiResponse.<ConsultationShelterDTO>builder()
                .result(result)
                .code(1000)
                .message("Consultation Shelter created successfully")
                .build();
    }

    @GetMapping("/request-detail/{requestDetailId}")
    public ApiResponse<List<ConsultationShelterDTO>> getConsultationSheltersByRequestDetail(
            @PathVariable Integer requestDetailId) {
        List<ConsultationShelterDTO> shelters = consultationShelterService.getSheltersByRequestDetail(requestDetailId);
        return ApiResponse.<List<ConsultationShelterDTO>>builder()
                .result(shelters)
                .code(1000)
                .message("Retrieved all consultation shelters for the request detail")
                .build();
    }
}
