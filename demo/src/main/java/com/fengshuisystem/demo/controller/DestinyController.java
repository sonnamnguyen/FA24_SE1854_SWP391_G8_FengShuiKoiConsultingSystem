package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.DestinyInputDTO;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponseContainer;
import com.fengshuisystem.demo.dto.response.CompatibilityResultResponse;
import com.fengshuisystem.demo.service.impl.AutoConsultationServiceImpl;
import com.fengshuisystem.demo.service.impl.CompatibilityResultResponseServiceImpl;
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

   AutoConsultationServiceImpl autoConsultationService;
   CompatibilityResultResponseServiceImpl compatibilityService;

    @GetMapping("/destiny/{yearOfBirth}")
    public ApiResponse<AutoConsultationResponseContainer> getElement(@PathVariable int yearOfBirth) {
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