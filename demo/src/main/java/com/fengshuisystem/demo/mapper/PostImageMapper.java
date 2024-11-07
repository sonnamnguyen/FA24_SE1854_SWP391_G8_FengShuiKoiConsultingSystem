package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.dto.PostImageDTO;
import com.fengshuisystem.demo.entity.AnimalImage;
import com.fengshuisystem.demo.entity.PostImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface PostImageMapper extends EntityMapper<PostImageDTO, PostImage> {
//    @Mapping(target = "post.id", source = "postId")
//   public PostImage toEntity(PostImageDTO dto);
}