package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.service.PostImageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post/images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostImageController {
    PostImageService postImageService;
    @PostMapping
    public ApiResponse<PostImageDTO> createPostImage(@RequestBody PostImageDTO postImageRequest) {
        return ApiResponse.<PostImageDTO>builder()
                .result(postImageService.createPostImage(postImageRequest))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<PostImageDTO> updatePostImage(@PathVariable Integer id, @RequestBody @Valid PostImageDTO postImageRequest) {
        return ApiResponse.<PostImageDTO>builder()
                .result(postImageService.updatePostImage(id, postImageRequest))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePostImage(@PathVariable Integer id) {
        postImageService.deletePostImage(id);
        return ApiResponse.<String>builder()
                .result("The post image has been deleted")
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<PostImageDTO>> getAllPostImages(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<PostImageDTO>>builder()
                .result(postImageService.getPostImages(page, size))
                .build();
    }
}
