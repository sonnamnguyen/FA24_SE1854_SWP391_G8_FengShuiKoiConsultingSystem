package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.service.impl.ConsultationAnimalServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultation-animals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationAnimalController {

    ConsultationAnimalServiceImpl consultationAnimalService;

    @PostMapping("/resultId/{resultId}/animal-category-id/{animalCategoryId}")
    public ApiResponse<ConsultationAnimalDTO> createConsultationAnimal(
            @RequestBody ConsultationAnimalDTO consultationAnimalDTO,
            @PathVariable Integer resultId,
            @PathVariable Integer animalCategoryId) {
        return ApiResponse.<ConsultationAnimalDTO>builder()
                .result(consultationAnimalService.createConsultationAnimal(consultationAnimalDTO, resultId, animalCategoryId))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ConsultationAnimalDTO>> getAllConsultationAnimalPage(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<ConsultationAnimalDTO>>builder()
                .result(consultationAnimalService.getAllConsultationAnimalPage(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ConsultationAnimalDTO> updateConsultationAnimal(@PathVariable Integer id, @Valid @RequestBody ConsultationAnimalDTO consultationAnimalDTO) {
        return ApiResponse.<ConsultationAnimalDTO>builder()
                .result(consultationAnimalService.updateConsultationAnimal(id, consultationAnimalDTO))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ConsultationAnimalDTO> getConsultationAnimalById(@PathVariable Integer id) {
        return ApiResponse.<ConsultationAnimalDTO>builder()
                .result(consultationAnimalService.getConsultationAnimalById(id))
                .build();
    }

    @GetMapping("/animalCategory")
    public ApiResponse<List<ConsultationAnimalDTO>> getAllConsultationAnimals() {
        return ApiResponse.<List<ConsultationAnimalDTO>>builder()
                .result(consultationAnimalService.getAllConsultationAnimals())
                .build();
    }
}
