package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.Destiny2DTO;
import com.fengshuisystem.demo.entity.ConsultationDestiny;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface Destiny2Mapper extends EntityMapper<Destiny2DTO, ConsultationDestiny> {

}
