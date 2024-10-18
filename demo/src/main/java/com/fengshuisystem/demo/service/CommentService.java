package com.fengshuisystem.demo.service;
import com.fengshuisystem.demo.dto.CommentDTO;
import com.fengshuisystem.demo.dto.PageResponse;

public interface CommentService {
    public CommentDTO createComment(CommentDTO commentDTO);
    public void deleteComment(Integer id);
    public CommentDTO updateComment(Integer id, CommentDTO commentDTO);
    public PageResponse<CommentDTO> getComments(int page, int size);


}
