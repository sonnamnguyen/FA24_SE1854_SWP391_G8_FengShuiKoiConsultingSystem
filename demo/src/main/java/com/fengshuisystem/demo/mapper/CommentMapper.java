package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.CommentDTO;
import com.fengshuisystem.demo.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
}