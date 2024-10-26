package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.NumberDTO;
import com.fengshuisystem.demo.service.NumberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NumberController {
    NumberService numberService;
    @GetMapping("/number")
    public ApiResponse<List<NumberDTO>> getALlNumber() {
        return ApiResponse.<List<NumberDTO>>builder()
                .result(numberService.getAllNumbers())
                .build();
    }
}
