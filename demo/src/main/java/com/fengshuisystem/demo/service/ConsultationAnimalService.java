package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ConsultationAnimalService {
    ConsultationAnimalDTO createConsultationAnimal(ConsultationAnimalDTO dto, Integer resultId, Integer animalCategoryId);

    PageResponse<ConsultationAnimalDTO> getAllConsultationAnimalPage(int page, int size);

    ConsultationAnimalDTO updateConsultationAnimal(Integer id, @Valid ConsultationAnimalDTO consultationAnimalDTO);

    ConsultationAnimalDTO getConsultationAnimalById(Integer id);

    List<ConsultationAnimalDTO> getAllConsultationAnimals();
}