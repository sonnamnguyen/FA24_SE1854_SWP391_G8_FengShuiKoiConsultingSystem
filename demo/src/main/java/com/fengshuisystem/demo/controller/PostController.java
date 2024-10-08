package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.entity.Post;
import com.fengshuisystem.demo.service.impl.PostServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/posts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

public class PostController {
    PostServiceImpl postService;
    // Get all posts
    @GetMapping
    public ApiResponse<List<Post>> getAllPosts() {
        return ApiResponse.<List<Post>>builder()
                .result(postService.getAllPosts())
                .build();
    }

    // Get post by ID
    @GetMapping("/{id}")
    public ApiResponse<Post> getPostById(@PathVariable Integer id) {
        return ApiResponse.<Post>builder()
                .result(postService.getPostById(id))
                .build();

    }

    // Create a new post
    @PostMapping
    public ApiResponse<Post> createPost(@RequestBody Post post) {
        return ApiResponse.<Post>builder()
                .result(postService.createPost(post)).build();
    }

    // Update an existing post
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Integer id, @RequestBody Post postDetails) {
        Post updatedPost =
                postService.updatePost(id, postDetails);
        return ResponseEntity.ok(updatedPost); // Trả về mã trạng thái 200 (OK)
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.delete(id);
        return ResponseEntity.noContent().build(); // Trả về mã trạng thái 204 (No Content)

    }
}
