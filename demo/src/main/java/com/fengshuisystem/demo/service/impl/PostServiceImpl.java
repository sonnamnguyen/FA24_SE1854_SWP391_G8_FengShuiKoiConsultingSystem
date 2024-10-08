package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.entity.Post;
import com.fengshuisystem.demo.entity.PostCategory;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.repository.PostRepository;
import com.fengshuisystem.demo.service.PostService;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void delete(Integer id) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            postRepository.deleteById(id);
        } else {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }
    }


    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Integer id, Post postDetails) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
            if (post != null) {
                post.setContent(postDetails.getContent());
                post.setLikeNumber(postDetails.getLikeNumber());
                post.setDislikeNumber(postDetails.getDislikeNumber());
                post.setShareNumber(postDetails.getShareNumber());
                post.setStatus(postDetails.getStatus());
                post.setCreateDate(postDetails.getCreateDate());
                post.setUpdateDate(postDetails.getUpdateDate());
                post.setCreateBy(postDetails.getCreateBy());
                post.setUpdateBy(postDetails.getUpdateBy());
                return postRepository.save(post);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
    }
}
