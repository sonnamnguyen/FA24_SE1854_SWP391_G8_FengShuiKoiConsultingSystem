package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ConsultationShelterService {
    public ConsultationShelterDTO createConsultationShelter(
            ConsultationShelterDTO dto, Integer resultId, Integer shelterCategoryId);

    PageResponse<ConsultationShelterDTO> getAllConsultationShelterPage(int page, int size);

    ConsultationShelterDTO updateConsultationShelter(Integer id, @Valid ConsultationShelterDTO consultationShelterDTO);

    ConsultationShelterDTO getConsultationShelterById(Integer id);

    List<ConsultationShelterDTO> getAllConsultationShelter();
}
