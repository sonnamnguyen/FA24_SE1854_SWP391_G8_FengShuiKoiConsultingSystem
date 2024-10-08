package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.entity.Post;

import java.util.List;

public interface PostService {
    public void delete(Integer id);
    public Post createPost(Post post);
    public Post updatePost(Integer id, Post post);
    public List<Post> getAllPosts();
    public Post getPostById(Integer id);
}
