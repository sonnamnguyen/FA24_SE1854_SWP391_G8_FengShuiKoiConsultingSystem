package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.service.impl.ConsultationShelterServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultation-shelters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationShelterController {

    ConsultationShelterServiceImpl consultationShelterService;

    @PostMapping("/resultId/{resultId}/shelter-category-id/{shelterCategoryId}")
    public ApiResponse<ConsultationShelterDTO> createConsultationShelter(
            @RequestBody ConsultationShelterDTO consultationShelterDTO,
            @PathVariable Integer resultId,
            @PathVariable Integer shelterCategoryId) {
        ConsultationShelterDTO result = consultationShelterService.createConsultationShelter(
                consultationShelterDTO, resultId, shelterCategoryId);
        return ApiResponse.<ConsultationShelterDTO>builder()
                .result(result)
                .code(1000)
                .message("Consultation Shelter created successfully")
                .build();
    }
}
