
package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ColorDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShapeDTO;
import com.fengshuisystem.demo.service.ColorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ColorController {
    ColorService colorService;
    @PostMapping
    public ApiResponse<ColorDTO> createColor(@Valid @RequestBody ColorDTO colorRequest) {
        return ApiResponse.<ColorDTO>builder()
                .result(colorService.createColor(colorRequest))
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<ColorDTO>> getAllColors(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<ColorDTO>>builder()
                .result(colorService.getColors(page, size))
                .build();
    }

    @GetMapping("/search-colors")
    public ApiResponse<PageResponse<ColorDTO>> getColorsBySearch(
            @RequestParam String name,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size

    ) {
        return ApiResponse.<PageResponse<ColorDTO>>builder()
                .result(colorService.getColorByName(name, page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ColorDTO> updateColor(@PathVariable Integer id,@Valid @RequestBody ColorDTO colorRequest) {
        colorRequest.setId(id);
        return ApiResponse.<ColorDTO>builder()
                .result(colorService.updateColor(id, colorRequest))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteColor(@PathVariable Integer id) {
        colorService.deleteColor(id);
        return ApiResponse.<String>builder()
                .result("The color has been deleted")
                .build();
    }

    @GetMapping("/getAll-Colors")
    public ApiResponse<List<ColorDTO>> getAllColors() {
        return ApiResponse.<List<ColorDTO>>builder()
                .result(colorService.getAllColors())
                .build();
    }
}
