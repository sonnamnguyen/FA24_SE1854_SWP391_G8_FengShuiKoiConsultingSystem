package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import org.mapstruct.Mapper;


@Mapper(
        config = DefaultConfigMapper.class,
        uses = {AnimalImageMapper.class, ConsultationAnimalMapper.class,
                ConsultationRequestDetailMapper.class, ColorMapper.class}
)

public interface AnimalMapper extends EntityMapper<AnimalCategoryDTO, AnimalCategory> {

}
