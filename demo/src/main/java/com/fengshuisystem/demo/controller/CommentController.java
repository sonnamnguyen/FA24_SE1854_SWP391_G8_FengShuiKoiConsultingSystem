package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.CommentDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentController {
    CommentService commentService;
    @PostMapping
    public ApiResponse<CommentDTO> createComment(@RequestBody CommentDTO commentRequest) {
        return ApiResponse.<CommentDTO>builder()
                .result(commentService.createComment(commentRequest))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<CommentDTO> updateComment(@PathVariable Integer id, @RequestBody @Valid CommentDTO commentRequest) {
        return ApiResponse.<CommentDTO>builder()
                .result(commentService.updateComment(id, commentRequest))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ApiResponse.<String>builder()
                .result("The comment has been deleted")
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<CommentDTO>> getAllComments(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<CommentDTO>>builder()
                .result(commentService.getComments(page, size))
                .build();
    }
}
