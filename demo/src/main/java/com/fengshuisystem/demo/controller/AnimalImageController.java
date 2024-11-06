
package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.service.AnimalImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animal-images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AnimalImageController {
    AnimalImageService animalImageService;

    @PostMapping("/{id}")
    public ApiResponse<List<AnimalImageDTO>> deleteAllAnimalImages(
            @PathVariable Integer id,
            @RequestBody List<String> imageUrls) {

        return ApiResponse.<List<AnimalImageDTO>>builder()
                .result(animalImageService.getAllAnimalImage(id, imageUrls))
                .build();
    }
}
