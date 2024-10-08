package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ConsultationAnimalMapper extends EntityMapper<ConsultationAnimalDTO, ConsultationAnimal> {
}
