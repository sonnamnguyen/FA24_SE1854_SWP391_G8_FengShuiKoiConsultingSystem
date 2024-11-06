package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.service.impl.ConsultationResultServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultation-results")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationResultController {

    private final ConsultationResultServiceImpl consultationResultService;

    @PostMapping("/requestId/{requestId}")
    public ApiResponse<ConsultationResultDTO> createConsultationResult(
            @Valid @PathVariable Integer requestId,
            @RequestBody ConsultationResultDTO consultationResultDTO) {
        return ApiResponse.<ConsultationResultDTO>builder()
                .result(consultationResultService.createConsultationResult(requestId, consultationResultDTO))
                .build();
    }

    @PutMapping("/send-email/{resultId}")
    public ApiResponse<ConsultationResultDTO> updateConsultationResult(@PathVariable Integer resultId) {

        return ApiResponse.<ConsultationResultDTO>builder()
                .result(consultationResultService.updateConsultationResult(resultId))
                .build();
    }

}
