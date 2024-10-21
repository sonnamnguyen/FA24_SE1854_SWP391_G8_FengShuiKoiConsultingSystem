package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.request.UserCreationRequest;
import com.fengshuisystem.demo.dto.response.UserResponse;

public interface ConsultationRequestService {
    public ConsultationRequestResponse createConsultationRequest(ConsultationRequestCreationRequest request);
}
