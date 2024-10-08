package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.entity.PostCategory;

import java.util.List;
import java.util.Optional;

public interface PostCategoryService {
    List<PostCategory> getAllPostCategories();
    PostCategory getPostCategoryById(Integer id);
    PostCategory createPostCategory(PostCategory postCategory);
    PostCategory updatePostCategory(Integer id, PostCategory postCategory);
    void deletePostCategory(Integer id);
}
