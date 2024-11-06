package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ShelterImageDTO;
import com.fengshuisystem.demo.entity.ShelterImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ShelterImageMapper extends EntityMapper<ShelterImageDTO, ShelterImage>{
    @Mapping(target = "shelterCategory.id", source = "shelterCategoryId")
    public ShelterImage toEntity(ShelterImageDTO dto);
}