
package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.PostMapper;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.DestinyService;
import com.fengshuisystem.demo.service.PostService;


import com.fengshuisystem.demo.service.UserService;
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
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PostServiceImpl implements PostService {
    PostMapper postMapper;
    PostRepository postRepository;
    PostCategoryRepository postCategoryRepository;
    DestinyRepository destinyRepository;
    UserRepository userRepository;
    PackageRepository packageRepository;
    DestinyService destinyService;

    @PreAuthorize("hasRole('USER')")
    @Override
    public PostDTO createPost(PostDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        PostCategory postCategory = postCategoryRepository.findById(request.getPostCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_EXISTED));
        Destiny destiny = destinyRepository.findById(request.getDestiny().getId()).orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        Package pkg = packageRepository.findById(request.getPackageId().getId()).orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
        Account account = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postMapper.toEntity(request);
        post.setAccount(account);
        post.setDestiny(destiny);
        post.setPackageId(pkg);
        post.setPostCategory(postCategory);
        post.setStatus(Status.ACTIVE);
        post.setCreatedBy(name);
        post.setDislikeNumber(0);
        post.setLikeNumber(0);
        post.setShareNumber(0);
        for (PostImage postImage : post.getImages()) {
            postImage.setPost(post);
        }
        return postMapper.toDto(postRepository.save(post));
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public PageResponse<PostDTO> getPostsByCategory(String postCategory, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByPostCategory(postCategory, pageable);
        if (pageData.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }
        return PageResponse.<PostDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(postMapper::toDto).toList())
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public PageResponse<PostDTO> getPosts(int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByStatus(status, pageable);
        if (pageData.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }
        return PageResponse.<PostDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(postMapper::toDto).toList())
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public void deletePost(Integer id) {
        var post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        post.setStatus(Status.DELETED);
        postRepository.save(post);
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public PostDTO updatePost(Integer id, PostDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        PostCategory postCategory = postCategoryRepository.findById(request.getPostCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_EXISTED));
        Destiny destiny = destinyRepository.findById(request.getDestiny().getId()).orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        Package pkg = packageRepository.findById(request.getPackageId().getId()).orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
        post.setPostCategory(postCategory);
        post.setDestiny(destiny);
        post.setPackageId(pkg);
        postMapper.update(request, post);
        post.setAccount(account);
        post.setUpdatedBy(name);
        for (PostImage postImage : post.getImages()) {
            postImage.setPost(post);
        }
        post.setUpdatedDate(Instant.now());
        return postMapper.toDto(postRepository.save(post));
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public PageResponse<PostDTO> getPostByAccountEmail(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByAccount_Email(name, pageable);
        if (pageData.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }
        return PageResponse.<PostDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(postMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public PageResponse<PostDTO> getPostByTitle(String title, int page, int size) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByTitleContaining(title, pageable);
        if (pageData.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }
        return PageResponse.<PostDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(postMapper::toDto).toList())
                .build();

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public long getPostCount() {
        return postRepository.countAllPosts();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public PostDTO getPostById(Integer id) {
        var post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        return postMapper.toDto(post);

    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public PageResponse<PostDTO> getPostsByYear(int page, int size, Integer year) {
        String destiny = destinyService.getDestinyFromYear(year);
        String destinyTuongSinh = destinyService.findTuongSinhTruoc(destiny);

        // Setting up sorting and pagination
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Retrieve posts for both destinies
        var pageDataDestiny = postRepository.findAllByDestiny(pageable, destiny);
        var pageDataTuongSinh = postRepository.findAllByDestiny(pageable, destinyTuongSinh);

        // Combine the posts from both queries
        List<PostDTO> combinedContent = new ArrayList<>();  // Specify List<PostDTO>
        combinedContent.addAll(pageDataDestiny.getContent().stream().map(postMapper::toDto).toList());
        combinedContent.addAll(pageDataTuongSinh.getContent().stream().map(postMapper::toDto).toList());

        // If both results are empty, throw an exception
        if (combinedContent.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }

        // Returning the combined page response
        return PageResponse.<PostDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages((int) Math.ceil((double) (pageDataDestiny.getTotalElements() + pageDataTuongSinh.getTotalElements()) / size)) // Combine total page count based on both data sources
                .totalElements(pageDataDestiny.getTotalElements() + pageDataTuongSinh.getTotalElements()) // Total combined elements
                .data(combinedContent)  // Now correctly matches the expected type
                .build();
    }
}

