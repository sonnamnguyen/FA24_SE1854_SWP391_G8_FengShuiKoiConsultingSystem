package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;

public interface ConsultationRequestDetailService {
    ConsultationRequestDetailDTO createConsultationRequestDetail(ConsultationRequestDetailDTO detailDTO, Integer requestId);
}
