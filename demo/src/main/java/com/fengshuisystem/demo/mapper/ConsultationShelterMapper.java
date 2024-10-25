//package com.fengshuisystem.demo.mapper;
//
//import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
//import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
//import com.fengshuisystem.demo.entity.ConsultationAnimal;
//import com.fengshuisystem.demo.entity.ConsultationShelter;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(config = DefaultConfigMapper.class)
//public interface ConsultationShelterMapper extends EntityMapper<ConsultationShelterDTO, ConsultationShelter> {
//
//    @Mapping(target = "consultationResult.id", source = "consultationId")
//    @Mapping(target = "shelterCategory.id", source = "shelterCategoryId")
//    ConsultationShelter toEntity(ConsultationShelterDTO dto);
//
//    @Mapping(target = "consultationId", source = "consultationResult.id")
//    @Mapping(target = "shelterCategoryId", source = "shelterCategory.id")
//    ConsultationShelterDTO toDto(ConsultationShelter entity);
//}

// ConsultationShelterMapper.java
package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = DefaultConfigMapper.class)
public interface ConsultationShelterMapper {

    @Mapping(target = "consultationResult.id", source = "consultationResult.id")
    @Mapping(target = "shelterCategoryId", source = "shelterCategory.id")
    ConsultationShelterDTO toDto(ConsultationShelter entity);

    @Mapping(target = "consultationResult.id", source = "consultationResult.id")
    @Mapping(target = "shelterCategory.id", source = "shelterCategoryId")
    ConsultationShelter toEntity(ConsultationShelterDTO dto);
}
