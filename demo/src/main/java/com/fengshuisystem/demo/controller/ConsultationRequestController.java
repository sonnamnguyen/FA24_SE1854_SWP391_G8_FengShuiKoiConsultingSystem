package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.service.impl.ConsultationRequestServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultation-requests")
@RequiredArgsConstructor
public class ConsultationRequestController {

    private final ConsultationRequestServiceImpl consultationRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConsultationRequestDTO>> createRequest(@Valid @RequestBody ConsultationRequestDTO requestDTO) {
        try {
            ConsultationRequestDTO result = consultationRequestService.createRequest(requestDTO);
            return ResponseEntity.ok(
                    ApiResponse.<ConsultationRequestDTO>builder()
                            .code(1000)
                            .message("Consultation Request created successfully")
                            .result(result)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ConsultationRequestDTO>builder()
                            .code(400)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}