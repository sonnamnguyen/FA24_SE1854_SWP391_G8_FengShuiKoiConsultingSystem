package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.DirectionDTO;
import com.fengshuisystem.demo.service.DirectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DirectionController {
    DirectionService directionService;
    @GetMapping("direction")
    public ApiResponse<List<DirectionDTO>> getAllDirections() {
        List<DirectionDTO> response = directionService.getAllDirections();
        return ApiResponse.<List<DirectionDTO>>builder()
                .result(response)
                .build();
    }
}
