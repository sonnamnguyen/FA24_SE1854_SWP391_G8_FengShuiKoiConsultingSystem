package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;

import java.util.List;

public interface ConsultationShelterService {
    public ConsultationShelterDTO createConsultationShelter(
            ConsultationShelterDTO dto, Integer resultId, Integer shelterCategoryId);
}
