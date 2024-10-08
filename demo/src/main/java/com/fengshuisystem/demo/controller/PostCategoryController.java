package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.entity.PostCategory;
import com.fengshuisystem.demo.service.PostCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/post/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PostCategoryController {
    PostCategoryService postCategoryService;
    // Lấy tất cả PostCategories
    @GetMapping
    public ResponseEntity<List<PostCategory>> getAllPostCategories() {
        List<PostCategory> postCategories = postCategoryService.getAllPostCategories();
        return ResponseEntity.ok(postCategories);
    }

    // Lấy PostCategory theo ID
    @GetMapping("/{id}")
    public ApiResponse<PostCategory> getPostCategoryById(@PathVariable Integer id) {
        return ApiResponse.<PostCategory>builder()
                .result(postCategoryService.getPostCategoryById(id))
                .build();
    }

    // Tạo mới PostCategory
    @PostMapping
    public ResponseEntity<PostCategory> createPostCategory(@RequestBody PostCategory postCategory) {
        PostCategory createdCategory = postCategoryService.createPostCategory(postCategory);
        return ResponseEntity.status(201).body(createdCategory); // Trả về mã trạng thái 201 (Created)
    }

    // Cập nhật PostCategory theo ID
    @PutMapping("/{id}")
    public ResponseEntity<PostCategory> updatePostCategory(@PathVariable Integer id, @RequestBody PostCategory postCategory) {
        try {
            PostCategory updatedCategory = postCategoryService.updatePostCategory(id, postCategory);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa PostCategory theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostCategory(@PathVariable Integer id) {
        postCategoryService.deletePostCategory(id);
        return ResponseEntity.noContent().build(); // Trả về mã trạng thái 204 (No Content)

    }
}
