
package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.service.ConsulationShelterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulation-shelter")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsulationShelterController {
    ConsulationShelterService consulationShelterService;


    @PostMapping
    public ApiResponse<ConsultationShelterDTO> createConsulationShelter(
            @RequestParam Integer id,
            @RequestParam List<Integer> ids,
            @RequestBody ConsultationShelterDTO consultationShelterDTO) {
        return ApiResponse.<ConsultationShelterDTO>builder()
                .result(consulationShelterService.createConsulationShelter(id, ids, consultationShelterDTO))
                .build();
    }
}
