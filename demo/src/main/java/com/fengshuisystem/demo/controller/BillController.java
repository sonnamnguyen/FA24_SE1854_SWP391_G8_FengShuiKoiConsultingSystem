package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.service.BillService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/bills")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BillController {
    BillService billService;
    @PostMapping
    public ApiResponse<BillDTO> createBill(@RequestBody BillDTO billRequest) {
        return ApiResponse.<BillDTO>builder()
                .result(billService.createBill(billRequest))
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<BillDTO>> getAllBills(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<BillDTO>>builder()
                .result(billService.getBills(page, size))
                .build();
    }
    @GetMapping("/{accountId}/status")
    public ApiResponse<PageResponse<BillDTO>> getBillsByAccountIdAndStatus(
            @PathVariable int accountId,
            @RequestParam BillStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.<PageResponse<BillDTO>>builder()
                .result(billService.getBillsByAccountIdAndStatus(accountId,status,page,size))
                .build();
    }
    @GetMapping("/status")
    public ApiResponse<PageResponse<BillDTO>> getAllBillsByStatus(
            @RequestParam BillStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.<PageResponse<BillDTO>>builder()
                .result(billService.getAllBillsByStatus(                  status,page,size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BillDTO> updateBill(@PathVariable Integer id, @RequestBody @Valid BillDTO billRequest) {
        return ApiResponse.<BillDTO>builder()
                .result(billService.updateBill(id, billRequest))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBill(@PathVariable Integer id) {
       billService.deleteBill(id);
        return ApiResponse.<String>builder()
                .result("The post has been deleted")
                .build();
    }

    // Phần của Khôi
    @PostMapping("/request/{requestId}/payments/{paymentId}")
    public ApiResponse<BillDTO> createBill(
            @RequestBody @Valid BillDTO billDTO,
            @PathVariable Integer requestId,
            @PathVariable Integer paymentId
    ) {
        BillDTO result = billService.createBillByRequestAndPayment(billDTO, requestId, paymentId);
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

}





