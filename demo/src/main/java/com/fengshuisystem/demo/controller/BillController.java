package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.service.impl.BillServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/bills")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BillController {

    private final BillServiceImpl billService;

    @PostMapping("/request/{requestId}/payments/{paymentId}")
    public ApiResponse<BillDTO> createBill(
            @PathVariable Integer requestId,
            @PathVariable Integer paymentId
    ) {
        BillDTO result = billService.createBillByRequestAndPayment(requestId, paymentId);
        return ApiResponse.<BillDTO>builder().result(result).build();
    }

    @GetMapping("/{billId}")
    public ApiResponse<BillDTO> getBillById(
            @PathVariable Integer billId) {
        BillDTO result = billService.getBillById(billId);
        return ApiResponse.<BillDTO>builder().result(result).build();
    }

    // API for fetch + phân trang
    @GetMapping
    public ApiResponse<PageResponse<BillDTO>> getAllBills(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<BillDTO>>builder()
                .result(billService.getAllBills(page, size))
                .build();
    }

    @GetMapping("/find-all")
    public ApiResponse<List<BillDTO>> getAll(
    ) {
        return ApiResponse.<List<BillDTO>>builder()
                .result(billService.getAll())
                .build();
    }

    @PutMapping("/{billId}/status")
    public ResponseEntity<String> updateBillStatus(
            @PathVariable Integer billId,
            @RequestBody Map<String, String> statusUpdate) {
        String billStatus = statusUpdate.get("billStatus");
        String requestStatus = statusUpdate.get("requestStatus");

        billService.updateStatusAfterPayment(billId, BillStatus.valueOf(billStatus), Request.valueOf(requestStatus));
        return ResponseEntity.ok("Status updated!!");
    }

    @PostMapping("/vnpay/success")
    public ResponseEntity<?> handleVnPayResponse(
            @RequestParam("billId") Integer billId,
            @RequestParam("vnp_ResponseCode") String responseCode) {
        try {
            if ("00".equals(responseCode)) {
                billService.updateStatusAfterPayment(billId, BillStatus.PAID, Request.COMPLETED);

                Integer requestId = billService.getRequestIdByBillId(billId);
                String redirectUrl = String.format(
                        "/consultation-request-detail/create/request-id/%d/bill-id/%d", requestId, billId);

                Map<String, String> result = new HashMap<>();
                result.put("message", "Payment successful!");
                result.put("redirectUrl", redirectUrl);

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment failed, please try again later!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error in payment processing!");
        }
    }

    @GetMapping("/total-income-this-month")
    public ApiResponse<BigDecimal> getTotalIncomeThisMonth() {
        BigDecimal totalIncome = billService.getTotalIncomeThisMonth();
        return ApiResponse.<BigDecimal>builder()
                .result(totalIncome)
                .build();
    }

    // API search
    @GetMapping("/search")
    public ApiResponse<List<BillDTO>> searchBills(
            @RequestParam(value = "status", required = false) BillStatus status,
            @RequestParam(value = "createdBy", required = false) String createdBy,
            @RequestParam(value = "minTotalAmount", required = false) BigDecimal minTotalAmount,
            @RequestParam(value = "maxTotalAmount", required = false) BigDecimal maxTotalAmount,
            @RequestParam(value = "paymentMethod", required = false) String paymentMethod) {
        if (minTotalAmount == null) {
            minTotalAmount = BigDecimal.ZERO;
        }
        if (maxTotalAmount == null) {
            maxTotalAmount = BigDecimal.valueOf(1000000);
        }
        return ApiResponse.<List<BillDTO>>builder()
                .result(billService.searchBills(status, createdBy, minTotalAmount, maxTotalAmount, paymentMethod))
                .build();
    }

}
