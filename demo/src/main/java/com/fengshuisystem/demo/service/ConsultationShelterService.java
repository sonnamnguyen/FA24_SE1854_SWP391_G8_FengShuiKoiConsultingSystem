package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;

import java.util.List;

public interface ConsultationShelterService {
    ConsultationShelterDTO createConsultationShelter(ConsultationShelterDTO dto, Integer requestDetailId, Integer shelterCategoryId);
    List<ConsultationShelterDTO> getSheltersByRequestDetail(Integer requestDetailId);
}
