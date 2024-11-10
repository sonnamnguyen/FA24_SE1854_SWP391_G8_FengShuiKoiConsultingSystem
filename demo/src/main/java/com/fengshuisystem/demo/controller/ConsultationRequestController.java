package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.service.impl.ConsultationRequestServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{requestId}")
    public ApiResponse<ConsultationRequestDTO> getConsultationRequest(@PathVariable Integer requestId) {
        return ApiResponse.<ConsultationRequestDTO>builder()
                .result(consultationRequestService.findById(requestId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ConsultationRequestDTO>> getAllConsultationRequests() {
        return ApiResponse.<List<ConsultationRequestDTO>>builder()
                .result(consultationRequestService.findAllRequests())
                .build();
    }

    @GetMapping("/completed-count")
    public ApiResponse<Long> getCompletedConsultationRequests() {
        long completedCount = consultationRequestService.getCompletedConsultationRequestCount();
        return ApiResponse.<Long>builder()
                .result(completedCount)
                .build();
    }
}