package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.dto.CommentDTO;
import com.fengshuisystem.demo.entity.AnimalImage;
import com.fengshuisystem.demo.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "post.id", source = "postId")
    public Comment toEntity(CommentDTO dto);
}