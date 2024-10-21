package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;

import java.util.List;

public interface ConsultationRequestService {
    ConsultationRequestDTO createRequest(ConsultationRequestDTO requestDTO);
    List<ConsultationRequestDTO> getAllRequests();
}

