package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.PostDTO;
import com.fengshuisystem.demo.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostController {
    PostService postService;
    @PostMapping
    public ApiResponse<PostDTO> createPost(@RequestBody PostDTO postRequest) {
        return ApiResponse.<PostDTO>builder()
                .result(postService.createPost(postRequest))
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<PostDTO>> getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<PostDTO>>builder()
                .result(postService.getPosts(page, size))
                .build();
    }
    @GetMapping("/search-posts")
    public ApiResponse<PageResponse<PostDTO>> getPostsByCategory(
            @RequestParam String name,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size

    ) {
        return ApiResponse.<PageResponse<PostDTO>>builder()
                .result(postService.getPostsByCategory(name, page, size))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<PostDTO> updatePost(@PathVariable Integer id, @RequestBody @Valid PostDTO postRequest) {
        return ApiResponse.<PostDTO>builder()
                .result(postService.updatePost(id, postRequest))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ApiResponse.<String>builder()
                .result("The post has been deleted")
                .build();
    }
}
