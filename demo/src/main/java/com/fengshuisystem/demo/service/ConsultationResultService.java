package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import jakarta.validation.Valid;

public interface ConsultationResultService {
    ConsultationResultDTO createConsultationResult(Integer consultationRequestId, ConsultationResultDTO dto);

    ConsultationResultDTO updateConsultationResultAndSendMail(Integer resultId);

    PageResponse<ConsultationResultDTO> getAllConsultationResult(int page, int size);
    PageResponse<ConsultationResultDTO> getConsultationResultBySearch(String search, int page, int size);
    ConsultationResultDTO updateConsultationResult(Integer id, @Valid ConsultationResultDTO consulationResultDTO);
    public long countCompletedConsultations();
}
