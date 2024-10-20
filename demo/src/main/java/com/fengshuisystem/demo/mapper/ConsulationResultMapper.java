package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class


)
public interface ConsulationResultMapper extends EntityMapper<ConsultationResultDTO, ConsultationResult>{

}
