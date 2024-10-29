package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = DefaultConfigMapper.class)
public interface ConsultationAnimalMapper {

    @Mapping(target = "consultationResultId", source = "consultationResult.id")
    @Mapping(target = "animalCategoryId", source = "animalCategory.id")
    ConsultationAnimalDTO toDto(ConsultationAnimal animal);

    @Mapping(target = "consultationResult.id", source = "consultationResultId")
    @Mapping(target = "animalCategory.id", source = "animalCategoryId")
    ConsultationAnimal toEntity(ConsultationAnimalDTO animalDTO);
}
