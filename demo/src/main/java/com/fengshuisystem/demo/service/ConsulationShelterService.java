package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.dto.ConsultationShelterRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationShelter;

import java.util.List;

public interface ConsulationShelterService {
    public ConsultationShelterDTO createConsulationShelter(Integer id, List<Integer> ids, ConsultationShelterDTO consultationShelterDTO);

    ConsultationShelter createShelterConsultation(ConsultationShelterRequestDTO requestDTO);
}
