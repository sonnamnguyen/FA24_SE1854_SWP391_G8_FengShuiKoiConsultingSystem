package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.service.ConsultationResultService;
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

    ConsultationResultService consultationResultService;

    @PostMapping("/requestId/{requestId}")
    public ApiResponse<ConsultationResultDTO> createConsultationResult(
            @PathVariable Integer requestId,
            @RequestBody ConsultationResultDTO consultationResultDTO) {
        ConsultationResultDTO result = consultationResultService.createConsultationResult(requestId, consultationResultDTO);
        return ApiResponse.<ConsultationResultDTO>builder()
                .result(result)
                .code(1000)
                .message("Consultation Result created successfully")
                .build();
    }

    @PutMapping("/send-email/{resultId}")
    public ApiResponse<ConsultationResultDTO> updateConsultationResult(@PathVariable Integer resultId) {
        ConsultationResultDTO result = consultationResultService.updateConsultationResult(resultId);
        return ApiResponse.<ConsultationResultDTO>builder()
                .result(result)
                .code(1000)
                .message("Consultation Result updated and email sent successfully")
                .build();
    }

}
