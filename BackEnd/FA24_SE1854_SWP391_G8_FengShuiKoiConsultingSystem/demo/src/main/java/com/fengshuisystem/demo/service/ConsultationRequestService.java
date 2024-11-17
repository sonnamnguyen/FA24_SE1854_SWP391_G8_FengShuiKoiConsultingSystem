package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDTO;

import java.util.List;

public interface ConsultationRequestService {
    ConsultationRequestDTO createConsultationRequest(ConsultationRequestDTO requestDTO);

    ConsultationRequestDTO updateStatusConsultationRequest(Integer requestId);

    ConsultationRequestDTO findById(Integer id);

    List<ConsultationRequestDTO> findAllRequests();

    long getCompletedConsultationRequestCount();

    List<ConsultationRequestDTO> searchConsultationRequests(String fullName, String email, String phone);
}
