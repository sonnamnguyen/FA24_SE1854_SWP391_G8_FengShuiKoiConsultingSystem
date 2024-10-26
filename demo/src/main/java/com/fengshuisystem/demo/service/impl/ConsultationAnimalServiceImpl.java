package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import com.fengshuisystem.demo.entity.ConsultationResult;
import com.fengshuisystem.demo.mapper.ConsultationAnimalMapper;
import com.fengshuisystem.demo.repository.ConsultationAnimalRepository;
import com.fengshuisystem.demo.service.ConsultationAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationAnimalServiceImpl implements ConsultationAnimalService {

    @Override
    public ConsultationAnimalDTO createConsultationAnimal(ConsultationAnimalDTO dto, Integer requestDetailId, Integer animalCategoryId) {
        return null;
    }

    @Override
    public List<ConsultationAnimalDTO> getAnimalsByRequestDetail(Integer requestDetailId) {
        return List.of();
    }
}
