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
@RequestMapping("/api/consultation-animal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationAnimalController {

    ConsultationAnimalServiceImpl consultationAnimalService;

    @PostMapping("/request-detail/{requestDetailId}/animal-category/{animalCategoryId}")
    public ApiResponse<ConsultationAnimalDTO> createConsultationAnimal(
            @RequestBody ConsultationAnimalDTO consultationAnimalDTO,
            @PathVariable Integer requestDetailId,
            @PathVariable Integer animalCategoryId) {
        ConsultationAnimalDTO result = consultationAnimalService.createConsultationAnimal(
                consultationAnimalDTO, requestDetailId, animalCategoryId);
        return ApiResponse.<ConsultationAnimalDTO>builder()
                .result(result)
                .code(1000)
                .message("Consultation Animal created successfully")
                .build();
    }

    @GetMapping("/request-detail/{requestDetailId}")
    public ApiResponse<List<ConsultationAnimalDTO>> getConsultationAnimalsByRequestDetail(
            @PathVariable Integer requestDetailId) {
        List<ConsultationAnimalDTO> animals = consultationAnimalService.getAnimalsByRequestDetail(requestDetailId);
        return ApiResponse.<List<ConsultationAnimalDTO>>builder()
                .result(animals)
                .code(1000)
                .message("Retrieved all consultation animals for the request detail")
                .build();
    }
}
