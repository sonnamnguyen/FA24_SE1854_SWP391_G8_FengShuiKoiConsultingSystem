package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.Destiny2DTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.request.Destiny2CompatibilityRequest;
import com.fengshuisystem.demo.dto.response.Destiny2CompatibilityResponse;
import com.fengshuisystem.demo.dto.response.Destiny2Response;
import com.fengshuisystem.demo.service.Destiny2Service;
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
public class Destiny2Controller {

    Destiny2Service destiny2Service;



    @GetMapping("/destiny2/{yearOfBirth}")
    public ApiResponse<Destiny2Response> autoConsultation(@PathVariable int yearOfBirth) {
        return ApiResponse.<Destiny2Response>builder()
                .result(destiny2Service.getDestinyInfoByYear(yearOfBirth))
                .build();
    }

    @GetMapping("/compatibility2/{yearOfBirth}")
    public ApiResponse<Destiny2CompatibilityResponse> calculateCompatibility(
            @PathVariable int yearOfBirth,
            @RequestBody Destiny2CompatibilityRequest destinyInput) {
        Destiny2CompatibilityResponse response = destiny2Service.calculateCompatibility(yearOfBirth,destinyInput);
        return ApiResponse.<Destiny2CompatibilityResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/destiny2")
    public ApiResponse<Destiny2DTO> createDestiny(@RequestBody Destiny2DTO destiny2DTO) {
        Destiny2DTO result = destiny2Service.createDestiny(destiny2DTO);
        return ApiResponse.<Destiny2DTO>builder()
                .result(result)
                .build();
    }


    @GetMapping("/destinies")
    public ApiResponse<PageResponse<Destiny2DTO>> getDestinies(
            @RequestParam int page,
            @RequestParam int size) {
        PageResponse<Destiny2DTO> response = destiny2Service.getDestinies(page, size);
        return ApiResponse.<PageResponse<Destiny2DTO>>builder()
                .result(response)
                .build();
    }
}
