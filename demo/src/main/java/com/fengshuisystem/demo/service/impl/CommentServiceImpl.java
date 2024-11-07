package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.dto.CommentDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.CommentMapper;
import com.fengshuisystem.demo.repository.CommentRepository;
import com.fengshuisystem.demo.repository.PostRepository;
import com.fengshuisystem.demo.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    CommentMapper commentMapper;
    CommentRepository commentRepository;
    PostRepository postRepository;
    @PreAuthorize("hasRole('USER')")
    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(()->new AppException(ErrorCode.POST_NOT_EXISTED));
        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setPost(post);
        comment.setStatus(Status.ACTIVE);
        comment.setCreatedBy(name);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public void deleteComment(Integer id) {
        var comment = commentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        comment.setStatus(Status.DELETED);
        commentRepository.save(comment);

    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public CommentDTO updateComment(Integer id, CommentDTO commentDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(()->new AppException(ErrorCode.POST_NOT_EXISTED));
        commentMapper.update(commentDTO, comment);
        comment.setPost(post);
        comment.setUpdatedBy(name);
        comment.setUpdatedDate(Instant.now());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public PageResponse<CommentDTO> getComments(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = commentRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.COMMENT_NOT_EXISTED);
        }
        return PageResponse.<CommentDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(commentMapper::toDto).toList())
                .build();
    }
}
