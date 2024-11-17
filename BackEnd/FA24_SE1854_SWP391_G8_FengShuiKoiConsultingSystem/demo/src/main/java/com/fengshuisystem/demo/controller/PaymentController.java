package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.dto.PaymentDTO;
import com.fengshuisystem.demo.service.impl.PaymentServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentDTO> getPaymentById(@PathVariable("paymentId") Integer paymentId )
    {
        PaymentDTO result = paymentService.getPaymentById(paymentId);
        return ApiResponse.<PaymentDTO>builder().result(result).build();
    }

    @GetMapping("/find-all")
    public ApiResponse<List<PaymentDTO>> findAll() {
        return ApiResponse.<List<PaymentDTO>>builder()
                .result(paymentService.getAll())
                .build();
    }
}
