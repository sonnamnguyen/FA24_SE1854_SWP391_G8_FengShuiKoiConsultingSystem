package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.DirectionDTO;
import com.fengshuisystem.demo.entity.Direction;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface DirectionMapper extends EntityMapper<DirectionDTO, Direction> {
}