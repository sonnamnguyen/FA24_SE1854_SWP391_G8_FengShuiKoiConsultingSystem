package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;

import java.util.List;

public interface ConsultationAnimalService {
    public ConsultationAnimalDTO createConsultationAnimal(ConsultationAnimalDTO dto, Integer resultId, Integer animalCategoryId);
}