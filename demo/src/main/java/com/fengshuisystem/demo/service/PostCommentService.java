package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.entity.PostComment;

import java.util.List;
import java.util.Optional;

public interface PostCommentService {
    PostComment createPostComment(PostComment postComment);

    // Lấy tất cả PostComment
    List<PostComment> getAllPostComments();

    // Lấy một PostComment theo ID
    PostComment getPostCommentById(Integer postCommentId);

    // Cập nhật một PostComment
    PostComment updatePostComment(Integer id,PostComment postComment);

    // Xóa một PostComment theo ID
    void deletePostComment(Integer postCommentId);
}
