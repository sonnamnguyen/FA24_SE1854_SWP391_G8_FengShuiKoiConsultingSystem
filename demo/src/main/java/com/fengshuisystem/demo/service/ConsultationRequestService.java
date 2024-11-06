package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;

public interface ConsultationRequestService {
    ConsultationRequestDTO createConsultationRequest(ConsultationRequestDTO requestDTO);

    ConsultationRequestDTO updateStatusConsultationRequest(Integer requestId);
}
