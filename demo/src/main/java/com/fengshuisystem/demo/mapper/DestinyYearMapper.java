package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.DestinyYearDTO;
import com.fengshuisystem.demo.entity.DestinyYear;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface DestinyYearMapper extends EntityMapper<DestinyYearDTO, DestinyYear> {

}
