package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.NumberDTO;
import com.fengshuisystem.demo.service.NumberService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/numbers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NumberController {

    NumberService numberService;

    @PutMapping("/{id}")
    public ApiResponse<NumberDTO> updateAnimal(@PathVariable Integer id, @RequestBody @Valid NumberDTO numberDTO) {
        return ApiResponse.<NumberDTO>builder()
                .result(numberService.updateNumber(id, numberDTO))
                .build();
    }
}
