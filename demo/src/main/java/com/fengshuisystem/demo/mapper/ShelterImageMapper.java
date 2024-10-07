package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ShelterImageDTO;
import com.fengshuisystem.demo.entity.ShelterImage;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface ShelterImageMapper extends EntityMapper<ShelterImageDTO, ShelterImage>{
}
