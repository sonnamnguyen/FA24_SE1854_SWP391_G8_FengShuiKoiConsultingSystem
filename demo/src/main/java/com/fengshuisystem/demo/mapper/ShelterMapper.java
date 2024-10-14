package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.entity.ShelterCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class


)
public interface ShelterMapper extends EntityMapper<ShelterCategoryDTO, ShelterCategory> {

    @Mapping(target = "shape.id", source = "shapeId")
     ShelterCategory toEntity(ShelterCategoryDTO dto);
}
