package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.service.impl.ConsultationRequestDetailServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultation-request-details")
@RequiredArgsConstructor
@Slf4j
public class ConsultationRequestDetailController {

    private final ConsultationRequestDetailServiceImpl consultationRequestDetailService;

    @PostMapping("/{packageId}")
    public ApiResponse<ConsultationRequestDetailDTO> createOrUpdateConsultationRequestDetail(
            @Valid @RequestBody ConsultationRequestDetailDTO requestDTO, @PathVariable Integer packageId) {
        ConsultationRequestDetailDTO result = consultationRequestDetailService.createOrUpdateDetail(requestDTO, packageId);
        return ApiResponse.<ConsultationRequestDetailDTO>builder().result(result).build();
    }

    @GetMapping("/{requestDetailId}")
    public ApiResponse<ConsultationRequestDetailDTO> getRequestDetailById(@PathVariable Integer requestDetailId) {
        ConsultationRequestDetailDTO detail = consultationRequestDetailService.getRequestDetailById(requestDetailId);
        return ApiResponse.<ConsultationRequestDetailDTO>builder().result(detail).build();
    }

    @DeleteMapping("cancel/{requestDetailId}")
    public ApiResponse<Void> cancelRequestDetail(@PathVariable Integer requestDetailId) {
        consultationRequestDetailService.cancelRequestDetailById(requestDetailId);
        return ApiResponse.<Void>builder().result(null).build();
    }
}
