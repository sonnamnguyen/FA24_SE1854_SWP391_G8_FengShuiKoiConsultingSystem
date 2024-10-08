package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.entity.PostComment;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.repository.PostCommentRepository;
import com.fengshuisystem.demo.service.PostCommentService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class PostCommentServiceImpl implements PostCommentService {
    private final PostCommentRepository postCommentRepository;

    public PostCommentServiceImpl(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    @Override
    public PostComment createPostComment(PostComment postComment) {
        return postCommentRepository.save(postComment);
    }

    @Override
    public List<PostComment> getAllPostComments() {
        return postCommentRepository.findAll();
    }

    @Override
    public PostComment getPostCommentById(Integer postCommentId) {
        return postCommentRepository.findById(postCommentId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_COMMENT_NOT_EXISTED));
    }

    @Override
    public PostComment updatePostComment(Integer id,PostComment postComment) {
        Optional<PostComment> existingPostComment = postCommentRepository.findById(id);
        if (existingPostComment.isPresent()) {
            return postCommentRepository.save(postComment);}
        else {
            throw new AppException(ErrorCode.POST_COMMENT_NOT_EXISTED);
        }
    }

    @Override
    public void deletePostComment(Integer id) {
        Optional<PostComment> existingPostComment = postCommentRepository.findById(id);
        if (existingPostComment.isPresent()) {
            postCommentRepository.deleteById(id);}
        else {
            throw new AppException(ErrorCode.POST_COMMENT_NOT_EXISTED);
        }
    }
}
