package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponseContainer;
import com.fengshuisystem.demo.service.impl.DestinyServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DestinyController {

    DestinyServiceImpl destinyService;

    @GetMapping("/destiny/{yearOfBirth}")
    public ApiResponse<AutoConsultationResponseContainer> getElement(@PathVariable int yearOfBirth) {
        return ApiResponse.<AutoConsultationResponseContainer>builder()
                .result(destinyService.autoConsultationResponseContainer(yearOfBirth))
                .build();
    }
}