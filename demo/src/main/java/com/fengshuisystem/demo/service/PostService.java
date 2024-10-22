package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PostDTO;
import com.fengshuisystem.demo.dto.PageResponse;

public interface PostService {
    public PostDTO createPost(PostDTO postDTO);
    public PageResponse<PostDTO> getPostsByCategory(String postCategory, int page, int size);
    public PageResponse<PostDTO> getPosts(int page, int size);
    public void deletePost(Integer id);
    public PostDTO updatePost(Integer id, PostDTO postDTO);
}