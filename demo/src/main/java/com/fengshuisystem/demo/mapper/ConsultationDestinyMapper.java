package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationDestinyDTO;
import com.fengshuisystem.demo.entity.ConsultationDestiny;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface ConsultationDestinyMapper extends EntityMapper<ConsultationDestinyDTO, ConsultationDestiny> {
}
