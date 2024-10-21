package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ConsultationRequestDetailMapper {

    ConsultationRequestDetail toEntity(ConsultationRequestDetailDTO dto);

    ConsultationRequestDetailDTO toDTO(ConsultationRequestDetail entity);
}
