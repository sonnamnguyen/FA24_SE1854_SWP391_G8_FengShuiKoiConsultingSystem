package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationDestinyDTO;
import com.fengshuisystem.demo.dto.PageResponse;
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

    @PostMapping("/consultation_destiny")
    public ApiResponse<ConsultationDestinyDTO> createDestiny(@RequestBody ConsultationDestinyDTO consultationDestinyDTO) {
        ConsultationDestinyDTO result = consultationDestinyService.createDestiny(consultationDestinyDTO);
        return ApiResponse.<ConsultationDestinyDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/consultation_destiny")
    public ApiResponse<PageResponse<ConsultationDestinyDTO>> getDestinyYears(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        PageResponse<ConsultationDestinyDTO> response = consultationDestinyService.getDestinies(page, size);
        return ApiResponse.<PageResponse<ConsultationDestinyDTO>>builder()
                .result(response)
                .build();
    }

    @PutMapping("/consultation_destiny/{id}")
    public ApiResponse<ConsultationDestinyDTO> updateDestinyYear(@PathVariable Integer id,@RequestBody ConsultationDestinyDTO consultationDestinyDTO) {
        ConsultationDestinyDTO result = consultationDestinyService.updateDestiny(id,consultationDestinyDTO);
        return ApiResponse.<ConsultationDestinyDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/consultation_destiny/{id}")
    public ApiResponse<String> deleteDestinyYear(@PathVariable Integer id) {
        consultationDestinyService.deleteDestiny(id);
        return ApiResponse.<String>builder()
                .result("The Destiny has been deleted")
                .build();
    }

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
