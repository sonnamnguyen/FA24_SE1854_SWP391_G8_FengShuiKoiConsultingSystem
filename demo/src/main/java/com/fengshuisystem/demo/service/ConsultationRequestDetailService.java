package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

public interface ConsultationRequestDetailService {
    ConsultationRequestDetailDTO createConsultationRequestDetail(ConsultationRequestDetailDTO detailDTO, Integer requestId);

    ConsultationRequestDetailDTO findById(@PathVariable Integer id);

    PageResponse<ConsultationRequestDetailDTO> getAllConsultationRequestDetail(int page, int size);

    ConsultationRequestDetailDTO updateConsultationRequestDetail(Integer id, @Valid ConsultationRequestDetailDTO consultationRequestDetailDTO);
}
