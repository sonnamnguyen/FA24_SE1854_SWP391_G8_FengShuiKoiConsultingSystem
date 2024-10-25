package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.service.impl.ConsultationRequestServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultation-requests")
@RequiredArgsConstructor
@Slf4j
public class ConsultationRequestController {

    private final ConsultationRequestServiceImpl consultationRequestService;

    @PostMapping
    public ApiResponse<ConsultationRequestDTO> createConsultationRequest(@Valid @RequestBody ConsultationRequestDTO requestDTO) {
        return ApiResponse.<ConsultationRequestDTO>builder()
                .result(consultationRequestService.createConsultationRequest(requestDTO))
                .build();
    }
}