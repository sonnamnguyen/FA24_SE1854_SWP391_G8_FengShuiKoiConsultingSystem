package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.AutoConsultationResponseDTO;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponseContainer;

public interface AutoConsultationService {
    AutoConsultationResponseDTO autoConsultationVipPro(int year);
    AutoConsultationResponseDTO autoConsultationVipPro2(int year);
    AutoConsultationResponseContainer autoConsultationResponseContainer(int year);
}
