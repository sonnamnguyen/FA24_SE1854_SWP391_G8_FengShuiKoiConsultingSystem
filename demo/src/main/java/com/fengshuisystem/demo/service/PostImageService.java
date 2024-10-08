package com.fengshuisystem.demo.service;


import com.fengshuisystem.demo.entity.PostImage;

import java.util.List;
import java.util.Optional;

public interface PostImageService {
    // Create a new post image
    PostImage createPostImage(PostImage postImage);

    // Get a post image by ID
    PostImage getPostImageById(Integer id);

    // Get all post images
    List<PostImage> getAllPostImages();

    // Update an existing post image
    PostImage updatePostImage(Integer id,PostImage postImageDetails);

    // Delete a post image by ID
    void deletePostImage(Integer id);
}
