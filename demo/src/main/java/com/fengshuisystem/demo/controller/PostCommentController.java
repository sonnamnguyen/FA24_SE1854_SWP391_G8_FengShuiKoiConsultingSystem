package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.entity.PostComment;
import com.fengshuisystem.demo.service.PostCommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/post/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PostCommentController {
    PostCommentService postCommentService;
    @PostMapping
    public ResponseEntity<PostComment> createPostComment(@RequestBody PostComment postComment) {
        PostComment createdComment = postCommentService.createPostComment(postComment);
        return ResponseEntity.ok(createdComment);
    }
    // Get all PostComments
    @GetMapping
    public ResponseEntity<List<PostComment>> getAllPostComments() {
        List<PostComment> comments = postCommentService.getAllPostComments();
        return ResponseEntity.ok(comments);
    }

    // Get a PostComment by ID
    @GetMapping("/{id}")
    public ApiResponse<PostComment> getPostCommentById(@PathVariable Integer id) {
        return ApiResponse.<PostComment>builder()
                .result(postCommentService.getPostCommentById(id))
                .build();
    }

    // Update a PostComment
    @PutMapping("/{id}")
    public ResponseEntity<PostComment> updatePostComment(@PathVariable Integer id, @RequestBody PostComment postComment) {
        PostComment updatedPostComment =
                postCommentService.updatePostComment(id, postComment);
        return ResponseEntity.ok(updatedPostComment); // Trả về mã trạng thái 200 (OK)
    }

    // Delete a PostComment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostComment(@PathVariable Integer id) {
        postCommentService.deletePostComment(id);
        return ResponseEntity.noContent().build(); // Trả về mã trạng thái 204 (No Content)

    }
}
