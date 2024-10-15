package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.dto.ConsultationAnimalRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationAnimal;

import java.util.List;

public interface ConsulationAnimalService {
    public ConsultationAnimalDTO createConsulationAnimal(Integer id, List<Integer> ids, ConsultationAnimalDTO consultationAnimalDTO) ;

    ConsultationAnimal createAnimalConsultation(ConsultationAnimalRequestDTO requestDTO);
    }
