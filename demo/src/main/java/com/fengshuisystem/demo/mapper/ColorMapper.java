package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ColorDTO;
import com.fengshuisystem.demo.entity.Color;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ColorMapper extends EntityMapper<ColorDTO, Color> {
    //    @Mapping(target = "destiny.id", source = "destinyId")
    Color toEntity(ColorDTO dto);
}