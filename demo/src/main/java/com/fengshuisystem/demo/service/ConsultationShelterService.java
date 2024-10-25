package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;

import java.util.List;

public interface ConsultationShelterService {
    public ConsultationShelterDTO createConsulationShelter(Integer id, List<Integer> ids, ConsultationShelterDTO consultationShelterDTO);
}
