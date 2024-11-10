package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.service.impl.ConsultationShelterServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultation-shelters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationShelterController {

    ConsultationShelterServiceImpl consultationShelterService;

    @PostMapping
    public ApiResponse<ConsultationShelterDTO> createConsultationShelter(
            @RequestBody ConsultationShelterDTO consultationShelterDTO) {
        return ApiResponse.<ConsultationShelterDTO>builder()
                .result(consultationShelterService.createConsultationShelter(consultationShelterDTO))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ConsultationShelterDTO>> getAllConsultationShelterPage(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<ConsultationShelterDTO>>builder()
                .result(consultationShelterService.getAllConsultationShelterPage(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ConsultationShelterDTO> updateConsultationShelter(@PathVariable Integer id, @Valid @RequestBody ConsultationShelterDTO consultationShelterDTO) {
        return ApiResponse.<ConsultationShelterDTO>builder()
                .result(consultationShelterService.updateConsultationShelter(id, consultationShelterDTO))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ConsultationShelterDTO> getConsultationShelterById(@PathVariable Integer id) {
        return ApiResponse.<ConsultationShelterDTO>builder()
                .result(consultationShelterService.getConsultationShelterById(id))
                .build();
    }

    @GetMapping("/shelterCategory")
    public ApiResponse<List<ConsultationShelterDTO>> getAllConsultationShelter() {
        return ApiResponse.<List<ConsultationShelterDTO>>builder()
                .result(consultationShelterService.getAllConsultationShelter())
                .build();
    }

    @GetMapping("/search-by-result-id")
    public ApiResponse<List<ConsultationShelterDTO>> searchByResultId(
            @RequestParam("resultId") Integer resultId) {
        return ApiResponse.<List<ConsultationShelterDTO>>builder()
                .result(consultationShelterService.searchByResultId(resultId))
                .build();
    }
}
