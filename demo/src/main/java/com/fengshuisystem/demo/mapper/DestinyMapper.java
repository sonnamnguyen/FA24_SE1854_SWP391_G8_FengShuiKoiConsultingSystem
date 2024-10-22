
package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.DestinyDTO;
import com.fengshuisystem.demo.entity.Destiny;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface DestinyMapper extends EntityMapper<DestinyDTO, Destiny>{

}
