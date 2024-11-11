
package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.service.AnimalService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AnimalController {
    AnimalService animalService;

    @PostMapping
    public ApiResponse<AnimalCategoryDTO> createAnimal(@Valid @RequestBody AnimalCategoryDTO animalCreationRequest) {
        System.out.println("Received request: " + animalCreationRequest); // Logging nội dung reques
        return ApiResponse.<AnimalCategoryDTO>builder()
                .result(animalService.createAnimal(animalCreationRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<AnimalCategoryDTO>> getAllAnimals(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<AnimalCategoryDTO>>builder()
                .result(animalService.getAnimals(page, size))
                .build();
    }

    @GetMapping("/animal-search")
    public ApiResponse<PageResponse<AnimalCategoryDTO>> getAnimalBySearch(
            @RequestParam String search,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size

    ) {
        return ApiResponse.<PageResponse<AnimalCategoryDTO>>builder()
                .result(animalService.getAnimalsBySearch(search, page, size))
                .build();
    }
    @GetMapping("/animal-destiny")
    public ApiResponse<PageResponse<AnimalCategoryDTO>> getAnimalByDestiny(
            @RequestParam List<String> destiny,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size

    ) {
        return ApiResponse.<PageResponse<AnimalCategoryDTO>>builder()
                .result(animalService.getAnimalsByDestiny(destiny, page, size))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<AnimalCategoryDTO> updateAnimal(@PathVariable Integer id, @Valid @RequestBody AnimalCategoryDTO animalCreationRequest) {
        return ApiResponse.<AnimalCategoryDTO>builder()
                .result(animalService.updateAnimal(id, animalCreationRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAnimal(@PathVariable Integer id) {
        animalService.deleteAnimal(id);
        return ApiResponse.<Void>builder()
                .message("The animal has been deleted")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AnimalCategoryDTO> getAnimalById(@PathVariable Integer id) {
        return ApiResponse.<AnimalCategoryDTO>builder()
                .result(animalService.getAnimalById(id))
                .build();
    }

    @GetMapping("/animalCategory")
    public ApiResponse<List<AnimalCategoryDTO>> getAllAnimals() {
        return ApiResponse.<List<AnimalCategoryDTO>>builder()
                .result(animalService.getAllAnimalCategory())
                .build();
    }
}
