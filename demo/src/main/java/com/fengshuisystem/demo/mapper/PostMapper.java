package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.PostDTO;
import com.fengshuisystem.demo.entity.Post;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface PostMapper extends EntityMapper<PostDTO, Post> {
}
