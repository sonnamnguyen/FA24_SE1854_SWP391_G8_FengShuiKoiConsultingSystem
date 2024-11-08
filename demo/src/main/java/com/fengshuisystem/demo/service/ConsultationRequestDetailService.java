package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface ConsultationRequestDetailService {
    ConsultationRequestDetailDTO createConsultationRequestDetail(ConsultationRequestDetailDTO detailDTO, Integer requestId);

    ConsultationRequestDetailDTO findById(@PathVariable Integer id);
}
