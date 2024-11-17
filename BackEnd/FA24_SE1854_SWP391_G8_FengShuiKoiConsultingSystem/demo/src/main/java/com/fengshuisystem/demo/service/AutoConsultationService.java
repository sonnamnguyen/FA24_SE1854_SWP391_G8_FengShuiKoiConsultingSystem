package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.response.AutoConsultationContainerResponse;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponse;

public interface AutoConsultationService {
    AutoConsultationResponse autoConsultationVipPro(int year);
    AutoConsultationResponse autoConsultationVipPro2(int year);
    AutoConsultationContainerResponse autoConsultationResponseContainer(int year);
}
