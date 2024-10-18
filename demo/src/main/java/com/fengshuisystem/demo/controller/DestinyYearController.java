package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.DestinyYearDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.service.DestinyYearService;
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
public class DestinyYearController {
    DestinyYearService destinyYearService;

    @PostMapping("/destiny-years")
    public ApiResponse<DestinyYearDTO> createDestinyYear(@RequestBody DestinyYearDTO destinyYearDTO) {
        DestinyYearDTO result = destinyYearService.createDestinyYear(destinyYearDTO);
        return ApiResponse.<DestinyYearDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/destiny-years")
    public ApiResponse<PageResponse<DestinyYearDTO>> getDestinyYears(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        PageResponse<DestinyYearDTO> response = destinyYearService.getDestinyYears(page, size);
        return ApiResponse.<PageResponse<DestinyYearDTO>>builder()
                .result(response)
                .build();
    }

    @PutMapping("/destiny-years/{id}")
    public ApiResponse<DestinyYearDTO> updateDestinyYear(@PathVariable Integer id,@RequestBody DestinyYearDTO destinyYearDTO) {
        DestinyYearDTO result = destinyYearService.updateDestinyYear(id,destinyYearDTO);
        return ApiResponse.<DestinyYearDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/destiny-years/{id}")
    public ApiResponse<String> deleteDestinyYear(@PathVariable Integer id) {
        destinyYearService.deleteDestinyYear(id);
        return ApiResponse.<String>builder()
                .result("The Year has been deleted")
                .build();
    }
}
