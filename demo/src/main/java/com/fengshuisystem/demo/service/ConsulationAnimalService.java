package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;

import java.util.List;

public interface ConsulationAnimalService {
    public ConsultationAnimalDTO createConsulationAnimal(Integer id, List<Integer> ids, ConsultationAnimalDTO consultationAnimalDTO) ;

    }
