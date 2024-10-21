package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;

public interface ConsultationRequestDetailService {
    ConsultationRequestDetail save(ConsultationRequestDetailDTO dto);
}
