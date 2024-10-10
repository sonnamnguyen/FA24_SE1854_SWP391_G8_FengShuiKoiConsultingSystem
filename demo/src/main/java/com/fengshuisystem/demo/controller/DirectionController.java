package com.fengshuisystem.demo.controller;


import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.DirectionDTO;
import com.fengshuisystem.demo.service.DirectionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direction")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DirectionController {

    DirectionService directionService;

    @PutMapping("/{id}")
    public ApiResponse<DirectionDTO> updateAnimal(@PathVariable Integer id, @RequestBody @Valid DirectionDTO directionDTO) {
        return ApiResponse.<DirectionDTO>builder()
                .result(directionService.updateDirection(id, directionDTO))
                .build();
    }
}
