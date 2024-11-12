package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PostDTO;
import com.fengshuisystem.demo.dto.PageResponse;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    PageResponse<PostDTO> getPostsByCategory(String postCategory, int page, int size);
    PageResponse<PostDTO> getPosts(int page, int size);
    void deletePost(Integer id);
    PostDTO updatePost(Integer id, PostDTO postDTO);
    PageResponse<PostDTO> getPostByAccountEmail(int page, int size);
    PageResponse<PostDTO> getPostByTitle(String title,int page, int size);
    long getPostCount();
    PostDTO getPostById(Integer id);
    PageResponse<PostDTO> getPostsByYear(int page, int size, Integer year);
}