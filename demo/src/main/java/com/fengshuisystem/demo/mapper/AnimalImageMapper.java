package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.entity.AnimalImage;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface AnimalImageMapper extends EntityMapper<AnimalImageDTO, AnimalImage> {

}
