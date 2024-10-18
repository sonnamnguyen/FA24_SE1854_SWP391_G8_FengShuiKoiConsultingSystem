package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;

public interface ConsultationRequestService {
    ApiResponse<String> createConsultationRequest(ConsultationRequestDTO requestDTO);
}
