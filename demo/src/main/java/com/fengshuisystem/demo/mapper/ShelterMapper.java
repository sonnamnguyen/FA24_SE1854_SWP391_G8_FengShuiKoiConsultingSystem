package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.entity.ShelterCategory;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface ShelterMapper extends EntityMapper<ShelterCategoryDTO, ShelterCategory> {

}