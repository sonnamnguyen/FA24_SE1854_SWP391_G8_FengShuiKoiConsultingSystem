package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.service.impl.ConsultationResultServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultation-results")
@Slf4j
public class ConsultationResultController {

    private final ConsultationResultServiceImpl consultationResultService;

    public ConsultationResultController (ConsultationResultServiceImpl consultationResultService) {
        this.consultationResultService = consultationResultService;
    }

    @PostMapping("/{consultationRequestDetailId}")
    public ApiResponse<ConsultationResultDTO> createConsultationResult(
            @Valid @RequestBody ConsultationResultDTO requestDTO,
            @PathVariable("consultationRequestDetailId") Integer consultationRequestDetailId) {

        ConsultationResultDTO resultDTO = consultationResultService.createConsultationResult(requestDTO, consultationRequestDetailId);

        return ApiResponse.<ConsultationResultDTO>builder()
                .result(resultDTO)
                .build();
    }
}
