package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.dto.CommentDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.PostImageDTO;
import com.fengshuisystem.demo.entity.Post;
import com.fengshuisystem.demo.entity.PostImage;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.PostImageMapper;
import com.fengshuisystem.demo.repository.PostImageRepository;
import com.fengshuisystem.demo.repository.PostRepository;
import com.fengshuisystem.demo.service.PostImageService;
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
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PostImageServiceImpl implements PostImageService {
    PostImageMapper postImageMapper;
    PostImageRepository postImageRepository;
    PostRepository  postRepository;

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<PostImageDTO> getAllPostImage(Integer id) {
        return postImageMapper.toDto(postImageRepository.findByPostId(id));
    }
}
