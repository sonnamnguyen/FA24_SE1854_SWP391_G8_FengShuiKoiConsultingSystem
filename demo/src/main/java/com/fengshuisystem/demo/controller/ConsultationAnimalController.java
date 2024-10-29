package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.service.impl.ConsultationAnimalServiceImpl;
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
        ConsultationAnimalDTO result = consultationAnimalService.createConsultationAnimal(
                consultationAnimalDTO, resultId, animalCategoryId);
        return ApiResponse.<ConsultationAnimalDTO>builder()
                .result(result)
                .code(1000)
                .message("Consultation Animal created successfully")
                .build();
    }
}
