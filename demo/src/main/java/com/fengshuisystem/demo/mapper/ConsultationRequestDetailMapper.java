package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ConsultationRequestDetailMapper extends EntityMapper<ConsultationRequestDetailDTO, ConsultationRequestDetail>{

}
