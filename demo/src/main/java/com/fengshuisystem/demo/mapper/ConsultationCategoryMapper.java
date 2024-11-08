package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ConsultationCategoryDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.ConsultationCategory;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)

public interface ConsultationCategoryMapper extends EntityMapper<ConsultationCategoryDTO, ConsultationCategory> {

}