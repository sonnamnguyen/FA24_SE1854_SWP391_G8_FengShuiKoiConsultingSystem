package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.service.ConsultationRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationRequestServiceImpl implements ConsultationRequestService {
    @Override
    public ConsultationRequestResponse createConsultationRequest(ConsultationRequestCreationRequest request) {
        return null;
    }
}
