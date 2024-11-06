
package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.DestinyDTO;
import com.fengshuisystem.demo.dto.request.DestinyRequest;
import com.fengshuisystem.demo.dto.response.AutoConsultationContainerResponse;
import com.fengshuisystem.demo.dto.response.CompatibilityResultResponse;
import com.fengshuisystem.demo.service.AutoConsultationService;
import com.fengshuisystem.demo.service.CompatibilityResultResponseService;
import com.fengshuisystem.demo.service.DestinyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinys")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DestinyController {
    DestinyService destinyService;
    AutoConsultationService autoConsultationService;
    CompatibilityResultResponseService compatibilityService;

    @GetMapping
    public ApiResponse<List<DestinyDTO>> getAllDestinys(
    ) {
        return ApiResponse.<List<DestinyDTO>>builder()
                .result(destinyService.getAllDestiny())
                .build();
    }
    @GetMapping("/destiny/{yearOfBirth}")
    public ApiResponse<String> getElement(@PathVariable int yearOfBirth) {
        return ApiResponse.<String>builder()
                .result(destinyService.getDestinyFromYear(yearOfBirth))
                .build();
    }

    @GetMapping("/autoConsultation/{yearOfBirth}")
    public ApiResponse<AutoConsultationContainerResponse> autoConsultation(@PathVariable int yearOfBirth) {
        return ApiResponse.<AutoConsultationContainerResponse>builder()
                .result(autoConsultationService.autoConsultationResponseContainer(yearOfBirth))
                .build();
    }

    @PostMapping ("/compatibility/{yearOfBirth}")
    public ApiResponse<CompatibilityResultResponse> calculateCompatibility(
            @PathVariable int yearOfBirth,
            @RequestBody DestinyRequest destinyInput) {
        CompatibilityResultResponse response = compatibilityService.calculateCompatibility(yearOfBirth, destinyInput);
        return ApiResponse.<CompatibilityResultResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping("/{destiny}")
    public ApiResponse<DestinyDTO> getAllDestinysTuongSinhAndTuongKhac(@PathVariable String destiny
    ) {
        return ApiResponse.<DestinyDTO>builder()
                .result(destinyService.getAllDestinyTuongSinhAndTuongKhac(destiny))
                .build();
    }
}
