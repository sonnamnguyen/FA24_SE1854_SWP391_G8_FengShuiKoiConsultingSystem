package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.BillDTO;
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

    @PutMapping("/{billId}/status")
    public ResponseEntity<String> updateBillStatus(
            @PathVariable Integer billId,
            @RequestBody Map<String, String> statusUpdate) {
        String billStatus = statusUpdate.get("billStatus");
        String requestStatus = statusUpdate.get("requestStatus");

        billService.updateStatusAfterPayment(billId, BillStatus.valueOf(billStatus), Request.valueOf(requestStatus));
        return ResponseEntity.ok("Trạng thái đã được cập nhật!");
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
                result.put("message", "Thanh toán thành công!");
                result.put("redirectUrl", redirectUrl);

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Thanh toán không thành công, vui lòng thử lại!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi trong quá trình xử lý thanh toán!");
        }
    }
    @GetMapping("/total-income-this-month")
    public ApiResponse<BigDecimal> getTotalIncomeThisMonth() {
        BigDecimal totalIncome = billService.getTotalIncomeThisMonth();
        return ApiResponse.<BigDecimal>builder()
                .result(totalIncome)
                .build();
    }
}





