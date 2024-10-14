package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.DestinyInputDTO;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponseContainer;
import com.fengshuisystem.demo.dto.response.CompatibilityResultResponse;
import com.fengshuisystem.demo.service.AutoConsultationService;
import com.fengshuisystem.demo.service.CompatibilityResultResponseService;

import com.fengshuisystem.demo.service.DestinyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DestinyController {

    DestinyService destinyService;
   AutoConsultationService autoConsultationService;
   CompatibilityResultResponseService compatibilityService;


    @GetMapping("/destiny/{yearOfBirth}")
    public ApiResponse<String> getElement(@PathVariable int yearOfBirth) {
        return ApiResponse.<String>builder()
                .result(destinyService.getDestinyFromYear(yearOfBirth))
                .build();
    }

    @GetMapping("/autoConsultation/{yearOfBirth}")
    public ApiResponse<AutoConsultationResponseContainer> autoConsultation(@PathVariable int yearOfBirth) {
        return ApiResponse.<AutoConsultationResponseContainer>builder()
                .result(autoConsultationService.autoConsultationResponseContainer(yearOfBirth))
                .build();
    }

    @GetMapping("/compatibility/{yearOfBirth}")
    public ApiResponse<CompatibilityResultResponse> calculateCompatibility(
            @PathVariable int yearOfBirth,
            @RequestBody DestinyInputDTO destinyInput) {
        CompatibilityResultResponse response = compatibilityService.calculateCompatibility(yearOfBirth, destinyInput);
        return ApiResponse.<CompatibilityResultResponse>builder()
                .result(response)
                .build();
    }

}