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
    private final DestinyYearService destinyYearService;

    @PostMapping("/destiny-years")
    public ApiResponse<DestinyYearDTO> createDestinyYear(@RequestBody DestinyYearDTO destinyYearDTO) {
        DestinyYearDTO result = destinyYearService.createDestinyYear(destinyYearDTO);
        return ApiResponse.<DestinyYearDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/destiny-years")
    public ApiResponse<PageResponse<DestinyYearDTO>> getDestinyYears(
            @RequestParam int page,
            @RequestParam int size) {
        PageResponse<DestinyYearDTO> response = destinyYearService.getDestinyYears(page, size);
        return ApiResponse.<PageResponse<DestinyYearDTO>>builder()
                .result(response)
                .build();
    }
}
