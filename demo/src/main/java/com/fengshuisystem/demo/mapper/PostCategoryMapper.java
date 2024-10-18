package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.PostCategoryDTO;
import com.fengshuisystem.demo.dto.PostDTO;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface PostCategoryMapper extends EntityMapper<PostCategoryDTO, PostDTO>{
}
