
package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.ConsultationAnimal;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ConsultationShelterMapper extends EntityMapper<ConsultationShelterDTO, ConsultationShelter> {
    @Mapping(target = "consultation.id", source = "consultationId")
    @Mapping(target = "shelterCategory.id", source = "shelterCategoryId")
    public ConsultationShelter toEntity(ConsultationShelterDTO dto);
}
