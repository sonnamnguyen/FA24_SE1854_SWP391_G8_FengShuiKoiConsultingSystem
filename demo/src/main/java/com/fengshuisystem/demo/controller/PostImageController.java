package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.entity.PostImage;
import com.fengshuisystem.demo.service.PostImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/post/images")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PostImageController {
    PostImageService postImageService;
    // Tạo mới PostImage
    @PostMapping
    public ResponseEntity<PostImage> createPostImage(@RequestBody PostImage postImage) {
        PostImage createdImage = postImageService.createPostImage(postImage);
        return ResponseEntity.status(201).body(createdImage); // Trả về mã trạng thái 201 (Created)
    }

    // Lấy tất cả PostImages
    @GetMapping
    public ResponseEntity<List<PostImage>> getAllPostImages() {
        List<PostImage> postImages = postImageService.getAllPostImages();
        return ResponseEntity.ok(postImages); // Trả về mã trạng thái 200 (OK)
    }

    // Lấy PostImage theo ID
    @GetMapping("/{id}")
    public ApiResponse<PostImage> getPostImageById(@PathVariable Integer id) {
        return ApiResponse.<PostImage>builder()
                .result(postImageService.getPostImageById(id))
                .build();
    }
    // Cập nhật PostImage theo ID
    @PutMapping("/{id}")
    public ResponseEntity<PostImage> updatePostImage(@PathVariable Integer id, @RequestBody PostImage postImage)
    {
        PostImage updatedPostImage = postImageService.updatePostImage(id, postImage);
        return ResponseEntity.ok(updatedPostImage); // Trả về mã trạng thái 200 (OK)
    }

    // Xóa PostImage theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostImage(@PathVariable Integer id) {
        postImageService.deletePostImage(id);
        return ResponseEntity.noContent().build(); // Trả về mã trạng thái 204 (No Content)

    }
}
