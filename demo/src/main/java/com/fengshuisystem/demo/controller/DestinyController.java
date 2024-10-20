package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.DestinyDTO;
import com.fengshuisystem.demo.service.DestinyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/destinys")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DestinyController {
    DestinyService destinyService;

    @GetMapping
    public ApiResponse<List<DestinyDTO>> getAllDestinys(
    ) {
        return ApiResponse.<List<DestinyDTO>>builder()
                .result(destinyService.getAllDestiny())
                .build();
    }
}
