package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.service.ConsultationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsultationRequestController {

    final ConsultationRequestService consultationRequestService;

    @PostMapping
    public ApiResponse<String> createRequest(@RequestBody ConsultationRequestDTO requestDTO) {
        return consultationRequestService.createConsultationRequest(requestDTO);
    }
}
