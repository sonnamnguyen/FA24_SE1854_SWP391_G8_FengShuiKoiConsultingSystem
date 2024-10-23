package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationRequestMapper {

    // xóa extends
    // cẩn thận Conflict
    // Ánh xạ từ DTO sang Entity
    @Mapping(source = "packageId", target = "packageField.id")
    ConsultationRequest toEntity(ConsultationRequestDTO requestDTO);

    // Ánh xạ từ Entity sang DTO
    @Mapping(source = "packageField.id", target = "packageId")
    ConsultationRequestDTO toDTO(ConsultationRequest request);
}
