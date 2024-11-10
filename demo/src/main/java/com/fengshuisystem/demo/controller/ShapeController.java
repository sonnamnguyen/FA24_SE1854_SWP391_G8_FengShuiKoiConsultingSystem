
package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShapeDTO;
import com.fengshuisystem.demo.entity.Shape;
import com.fengshuisystem.demo.service.ShapeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shapes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShapeController {

    ShapeService shapeService;

    @PostMapping
    public ApiResponse<ShapeDTO> createShape(@Valid @RequestBody ShapeDTO shapeRequest) {
        return ApiResponse.<ShapeDTO>builder()
                .result(shapeService.createShape(shapeRequest))
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<ShapeDTO>> getAllShapes(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<ShapeDTO>>builder()
                .result(shapeService.getShapes(page, size))
                .build();
    }

    @GetMapping("/shape-search")
    public ApiResponse<PageResponse<ShapeDTO>> getShapesBySearch(
            @RequestParam String name,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size

    ) {
        return ApiResponse.<PageResponse<ShapeDTO>>builder()
                .result(shapeService.getShapeByName(name, page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ShapeDTO> updateShape(@PathVariable Integer id,@Valid @RequestBody  ShapeDTO shapeRequest) {
        shapeRequest.setId(id);
        return ApiResponse.<ShapeDTO>builder()
                .result(shapeService.updateShape(id, shapeRequest))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteColor(@PathVariable Integer id) {
        shapeService.deleteShape(id);
        return ApiResponse.<String>builder()
                .result("The shape has been deleted")
                .build();
    }
    @GetMapping("/getAll-Shapes")
    public ApiResponse<List<ShapeDTO>> getAllShapes() {
        return ApiResponse.<List<ShapeDTO>>builder()
                .result(shapeService.getAllShapes())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ShapeDTO> getShape(@PathVariable Integer id) {
        return ApiResponse.<ShapeDTO>builder()
                .result(shapeService.getShapeById(id))
                .build();
    }
}
