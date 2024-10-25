
package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.service.ConsultationAnimalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulation-animal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsulationAnimalController {
    ConsultationAnimalService consultationAnimalService;

    @PostMapping
    public ApiResponse<ConsultationAnimalDTO> createConsulationAnimal(
            @RequestParam Integer id,
            @RequestParam List<Integer> ids,
            @RequestBody ConsultationAnimalDTO consultationAnimalDTO) {
        return ApiResponse.<ConsultationAnimalDTO>builder()
                .result(consultationAnimalService.createConsulationAnimal(id, ids, consultationAnimalDTO))
                .build();
    }


}
