package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationRequestMapper{

    // xóa extends
    // cẩn thận Conflict
    // Ánh xạ từ DTO sang Entity
    @Mapping(source = "packageId", target = "packageId.packageId")
    ConsultationRequest toEntity(ConsultationRequestDTO requestDTO);

    // Ánh xạ từ Entity sang DTO
    @Mapping(source = "packageId.packageId", target = "packageId")
    ConsultationRequestDTO toDTO(ConsultationRequest request);
}
