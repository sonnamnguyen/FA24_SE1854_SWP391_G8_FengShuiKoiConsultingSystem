package com.fengshuisystem.demo.service;
import com.fengshuisystem.demo.dto.CommentDTO;
import com.fengshuisystem.demo.dto.PageResponse;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);
    void deleteComment(Integer id);
    CommentDTO updateComment(Integer id, CommentDTO commentDTO);
    PageResponse<CommentDTO> getComments(int page, int size);
}
