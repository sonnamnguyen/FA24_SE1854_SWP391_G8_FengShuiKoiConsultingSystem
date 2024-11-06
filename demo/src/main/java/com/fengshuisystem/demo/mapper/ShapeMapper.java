package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ShapeDTO;
import com.fengshuisystem.demo.entity.Shape;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ShapeMapper extends EntityMapper<ShapeDTO, Shape> {
     //    @Mapping(target = "destiny.id", source = "destinyId")
     Shape toEntity(ShapeDTO dto);
}