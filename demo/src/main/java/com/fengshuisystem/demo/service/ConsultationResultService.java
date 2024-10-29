package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;

public interface ConsultationResultService {
    ConsultationResultDTO createConsultationResult(Integer consultationRequestId, ConsultationResultDTO dto);

    public ConsultationResultDTO updateConsultationResult(Integer resultId);
}
