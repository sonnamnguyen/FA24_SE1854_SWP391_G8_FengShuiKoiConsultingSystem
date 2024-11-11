package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.entity.NotificationFCM;
import com.fengshuisystem.demo.service.NotificationService;
import com.fengshuisystem.demo.service.impl.ConsultationResultServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultation-results")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationResultController {

    private final ConsultationResultServiceImpl consultationResultService;

    @PostMapping("/requestId/{requestId}")
    public ApiResponse<ConsultationResultDTO> createConsultationResult(
            @Valid @PathVariable Integer requestId,
            @RequestBody ConsultationResultDTO consultationResultDTO) {
        return ApiResponse.<ConsultationResultDTO>builder()
                .result(consultationResultService.createConsultationResult(requestId, consultationResultDTO))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ConsultationResultDTO>> getAllConsultationResult(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<ConsultationResultDTO>>builder()
                .result(consultationResultService.getAllConsultationResult(page, size))
                .build();
    }

    @GetMapping("/find-all")
    public ApiResponse<List<ConsultationResultDTO>> getAll(
    ) {
        return ApiResponse.<List<ConsultationResultDTO>>builder()
                .result(consultationResultService.getAll())
                .build();
    }

    @GetMapping("/user/{email}")
    public ApiResponse<List<ConsultationResultDTO>> getUserConsultationResults(@PathVariable("email") String email) {
        return ApiResponse.<List<ConsultationResultDTO>>builder()
                .result(consultationResultService.getUserConsultationResults(email))
                .build();
    }

    @GetMapping("/consultation-result-search")
    public ApiResponse<PageResponse<ConsultationResultDTO>> getConsultationResultBySearch(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam String search
    ) {
        return ApiResponse.<PageResponse<ConsultationResultDTO>>builder()
                .result(consultationResultService.getConsultationResultBySearch(search, page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ConsultationResultDTO> updateConsultationResult(@PathVariable Integer id, @RequestBody @Valid ConsultationResultDTO consulationResultDTO) {
        return ApiResponse.<ConsultationResultDTO>builder()
                .result(consultationResultService.updateConsultationResult(id, consulationResultDTO))
                .build();
    }

    @PutMapping("/send-email/{resultId}")
    public ApiResponse<ConsultationResultDTO> updateConsultationResultAndSendMail(@PathVariable Integer resultId) {

        return ApiResponse.<ConsultationResultDTO>builder()
                .result(consultationResultService.updateConsultationResultAndSendMail(resultId))
                .build();
    }
    @GetMapping("/completed-count")
    public long getCompletedConsultationsCount() {
        return consultationResultService.countCompletedConsultations();
    }
}
