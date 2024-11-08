package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationCategoryDTO;
import com.fengshuisystem.demo.service.impl.ConsultationCategoryServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consultation-category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationCategoryController {

    private final ConsultationCategoryServiceImpl consultationCategoryService;

    @GetMapping
    public ApiResponse<List<ConsultationCategoryDTO>> getAllConsultationCategory() {
        return ApiResponse.<List<ConsultationCategoryDTO>>builder()
                .result(consultationCategoryService.getAllConsultationCategory())
                .build();
    }
}
