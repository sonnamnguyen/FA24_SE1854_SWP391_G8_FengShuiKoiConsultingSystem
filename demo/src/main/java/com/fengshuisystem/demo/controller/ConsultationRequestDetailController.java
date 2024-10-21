package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import com.fengshuisystem.demo.service.ConsultationRequestDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.AnimalCategory;


@RestController
@RequestMapping("/api/consultation-request-details")
public class ConsultationRequestDetailController {

    private final ConsultationRequestDetailService service;

    public ConsultationRequestDetailController(ConsultationRequestDetailService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConsultationRequestDetailDTO> createRequestDetail(
            @RequestBody ConsultationRequestDetailDTO requestDetailDTO) {
        ConsultationRequestDetail savedDetail = service.save(requestDetailDTO);
        return ResponseEntity.ok(toDTO(savedDetail));
    }

    private ConsultationRequestDetailDTO toDTO(ConsultationRequestDetail entity) {
        return ConsultationRequestDetailDTO.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .status("COMPLETED")
                .shelterCategoryIds(entity.getShelterCategories().stream().map(ShelterCategory::getId).toList())
                .animalCategoryIds(entity.getAnimalCategories().stream().map(AnimalCategory::getId).toList())
                .createdBy(entity.getCreatedBy())
                .createdDate(entity.getCreatedDate())
                .updatedBy(entity.getUpdatedBy())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }
}
