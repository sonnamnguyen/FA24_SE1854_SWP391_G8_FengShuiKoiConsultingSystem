package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ConsultationAnimalMapper extends EntityMapper<ConsultationAnimalDTO, ConsultationAnimal> {
    @Mapping(target = "consultation.id", source = "consultationResultId")
    @Mapping(target = "animalCategory.id", source = "animalCategoryId")
    public ConsultationAnimal toEntity(ConsultationAnimalDTO dto);
}
