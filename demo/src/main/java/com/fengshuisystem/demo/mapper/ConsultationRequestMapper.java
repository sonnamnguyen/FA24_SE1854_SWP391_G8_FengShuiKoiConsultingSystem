package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultationRequestMapper {

    ConsultationRequestDTO toDto(ConsultationRequest entity);

    ConsultationRequest toEntity(ConsultationRequestDTO dto);
}
