package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.entity.PostImage;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.repository.PostImageRepository;
import com.fengshuisystem.demo.service.PostImageService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class PostImageServiceImpl implements PostImageService {
    private final PostImageRepository postImageRepository;

    public PostImageServiceImpl(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    @Override
    public PostImage createPostImage(PostImage postImage) {
        return postImageRepository.save(postImage);
    }

    @Override
    public PostImage getPostImageById(Integer id) {
        return postImageRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED));
    }

    @Override
    public List<PostImage> getAllPostImages() {
        return postImageRepository.findAll();
    }

    @Override
    public PostImage updatePostImage(Integer id, PostImage postImageDetails) {
        Optional<PostImage> existingPostImage = postImageRepository.findById(id);
        if (existingPostImage.isPresent()) {
            return postImageRepository.save(postImageDetails);}
        else {
            throw new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED);
        }
    }

    @Override
    public void deletePostImage(Integer id) {
        Optional<PostImage> existingPostImage = postImageRepository.findById(id);
        if (existingPostImage.isPresent()) {
            postImageRepository.deleteById(id);}
        else {
            throw new AppException(ErrorCode.POST_IMAGE_NOT_EXISTED);
        }
    }
}
