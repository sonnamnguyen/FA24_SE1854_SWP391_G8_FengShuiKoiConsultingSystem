package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.PostImageDTO;
import com.fengshuisystem.demo.entity.PostImage;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface PostImageMapper extends EntityMapper<PostImageDTO, PostImage> {
}