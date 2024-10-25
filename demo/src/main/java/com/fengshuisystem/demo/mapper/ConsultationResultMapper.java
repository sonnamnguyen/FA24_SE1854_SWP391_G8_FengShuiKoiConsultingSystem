package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = DefaultConfigMapper.class)
public interface ConsultationResultMapper {

    ConsultationResultDTO toDto(ConsultationResult entity);

    ConsultationResult toEntity(ConsultationResultDTO dto);
}
