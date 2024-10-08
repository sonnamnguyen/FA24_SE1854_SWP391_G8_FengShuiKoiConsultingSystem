package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ConsultationShelterMapper extends EntityMapper<ConsultationShelterDTO, ConsultationShelter> {
}
