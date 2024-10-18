package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.request.ConsultationDestinyCompatibilityRequest;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyCompatibilityResponse;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyResponse;
import com.fengshuisystem.demo.service.ConsultationDestinyService;
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
public class ConsultationDestinyController {

    ConsultationDestinyService consultationDestinyService;

    @GetMapping("/consultationDestiny/{yearOfBirth}")
    public ApiResponse<ConsultationDestinyResponse> autoConsultation(@PathVariable int yearOfBirth) {
        return ApiResponse.<ConsultationDestinyResponse>builder()
                .result(consultationDestinyService.getDestinyInfoByYear(yearOfBirth))
                .build();
    }

    @GetMapping("/compatibility/{yearOfBirth}")
    public ApiResponse<ConsultationDestinyCompatibilityResponse> calculateCompatibility(
            @PathVariable int yearOfBirth,
            @RequestBody ConsultationDestinyCompatibilityRequest destinyInput) {
        ConsultationDestinyCompatibilityResponse response = consultationDestinyService.calculateCompatibility(yearOfBirth,destinyInput);
        return ApiResponse.<ConsultationDestinyCompatibilityResponse>builder()
                .result(response)
                .build();
    }
}
