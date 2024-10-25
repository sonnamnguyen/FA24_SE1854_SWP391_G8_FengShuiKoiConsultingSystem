package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultConfigMapper.class)
public interface ConsultationAnimalMapper extends EntityMapper<ConsultationAnimalDTO, ConsultationAnimal> {

    @Mapping(target = "consultationResult.id", source = "consultationResultId")
    @Mapping(target = "animalCategory.id", source = "animalCategoryId")
    ConsultationAnimal toEntity(ConsultationAnimalDTO dto);

    @Mapping(target = "consultationResultId", source = "consultationResult.id")
    @Mapping(target = "animalCategoryId", source = "animalCategory.id")
    ConsultationAnimalDTO toDto(ConsultationAnimal entity);
}

