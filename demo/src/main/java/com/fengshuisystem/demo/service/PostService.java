package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PostDTO;
import com.fengshuisystem.demo.dto.PageResponse;

public interface PostService {
    public PostDTO createPost(PostDTO postDTO);
    public PageResponse<PostDTO> getPostsByCategory(String postCategory, int page, int size);
    public PageResponse<PostDTO> getPosts(int page, int size);
    public void deletePost(Integer id);
    public PostDTO updatePost(Integer id, PostDTO postDTO);
    public PageResponse<PostDTO> getPostByAccountEmail(int page, int size);
    public PageResponse<PostDTO> getPostByTitle(String title,int page, int size);
    public long getPostCount();
    public PostDTO getPostById(Integer id);
    PageResponse<PostDTO> getPostsByYear(int page, int size, Integer year);
}