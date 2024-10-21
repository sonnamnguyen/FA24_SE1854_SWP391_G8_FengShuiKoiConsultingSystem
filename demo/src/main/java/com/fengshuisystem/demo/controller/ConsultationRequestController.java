package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consultation-requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationRequestController {
    ConsultationRequestServiceImpl consultationRequestService;

    @PostMapping
    public ApiResponse<ConsultationRequestResponse> createConsultationRequest(@RequestBody @Valid ConsultationRequestCreationRequest request) {
        return ApiResponse.<ConsultationRequestResponse>builder()
                .result(consultationRequestService.createConsultationRequest(request))
                .build();
    }
}
