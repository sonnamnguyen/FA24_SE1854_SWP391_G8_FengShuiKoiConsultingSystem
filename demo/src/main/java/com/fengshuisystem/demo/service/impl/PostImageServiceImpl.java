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

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PostImageServiceImpl implements PostImageService {
    PostImageMapper postImageMapper;
    PostImageRepository postImageRepository;
    PostRepository  postRepository;
    @PreAuthorize("hasRole('USER')")
    @Override
    public PostImageDTO createPostImage(PostImageDTO postImageDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Post post = postRepository.findById(postImageDTO.getPost().getId()).orElseThrow(()->new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED));
        PostImage postImage = postImageMapper.toEntity(postImageDTO);
        postImage.setPost(post);
        postImage.setStatus(Status.ACTIVE);
        postImage.setCreatedBy(name);
        return postImageMapper.toDto(postImageRepository.save(postImage));
    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public void deletePostImage(Integer id) {
        var postImage = postImageRepository.findById(id).orElseThrow
                (() -> new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED));
        postImage.setStatus(Status.DELETED);
        postImageRepository.save(postImage);

    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public PostImageDTO updatePostImage(Integer id, PostImageDTO postImageDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        PostImage postImage = postImageRepository.findById(id).orElseThrow
                (() -> new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED));
        Post post = postRepository.findById(postImageDTO.getPost().getId()).orElseThrow(()->new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED));
        postImageMapper.update(postImageDTO, postImage);
        postImage.setPost(post);
        postImage.setUpdatedBy(name);
        postImage.setUpdatedDate(Instant.now());
        return postImageMapper.toDto(postImageRepository.save(postImage));
    }

    @Override
    public PageResponse<PostImageDTO> getPostImages(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postImageRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED);
        }
        return PageResponse.<PostImageDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(postImageMapper::toDto).toList())
                .build();
    }
}
