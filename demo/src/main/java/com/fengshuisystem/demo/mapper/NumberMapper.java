package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.NumberDTO;
import com.fengshuisystem.demo.entity.Number;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface NumberMapper extends EntityMapper<NumberDTO, Number>{
     Number toEntity(NumberDTO dto);
}