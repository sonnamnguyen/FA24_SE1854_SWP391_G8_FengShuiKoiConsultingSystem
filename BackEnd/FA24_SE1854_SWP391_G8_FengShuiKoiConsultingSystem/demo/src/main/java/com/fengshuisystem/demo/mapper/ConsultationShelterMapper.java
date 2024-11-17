package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = DefaultConfigMapper.class)
public interface ConsultationShelterMapper {

    @Mapping(target = "consultationResultId", source = "consultationResult.id")
    @Mapping(target = "shelterCategoryId", source = "shelterCategory.id")
    ConsultationShelterDTO toDto(ConsultationShelter shelter);

    @Mapping(target = "consultationResult.id", source = "consultationResultId")
    @Mapping(target = "shelterCategory.id", source = "shelterCategoryId")
    ConsultationShelter toEntity(ConsultationShelterDTO shelterDTO);
}
