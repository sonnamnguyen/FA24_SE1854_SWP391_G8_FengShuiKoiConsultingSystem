package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.AnimalImage;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface AnimalImageMapper extends EntityMapper<AnimalImageDTO, AnimalImage> {
    @Mapping(target = "animalCategory.id", source = "animalId")
    public AnimalImage toEntity(AnimalImageDTO dto);
}