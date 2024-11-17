package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
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

    @PostMapping("/request-id/{requestId}")
    public ApiResponse<ConsultationRequestDetailDTO> createConsultationRequestDetail(
            @Valid @RequestBody ConsultationRequestDetailDTO requestDTO, @PathVariable Integer requestId) {
        ConsultationRequestDetailDTO result =
                consultationRequestDetailService.createConsultationRequestDetail(requestDTO, requestId);
        return ApiResponse.<ConsultationRequestDetailDTO>builder().result(result).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ConsultationRequestDetailDTO> getConsultationRequestDetail(@PathVariable Integer id) {
        return ApiResponse.<ConsultationRequestDetailDTO>builder()
                .result(consultationRequestDetailService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ConsultationRequestDetailDTO>> getAllConsultationRequestDetail(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<ConsultationRequestDetailDTO>>builder()
                .result(consultationRequestDetailService.getAllConsultationRequestDetail(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ConsultationRequestDetailDTO> updateConsultationRequestDetail(@PathVariable Integer id, @RequestBody @Valid ConsultationRequestDetailDTO consultationRequestDetailDTO) {
        return ApiResponse.<ConsultationRequestDetailDTO>builder()
                .result(consultationRequestDetailService.updateConsultationRequestDetail(id, consultationRequestDetailDTO))
                .build();
    }

}
