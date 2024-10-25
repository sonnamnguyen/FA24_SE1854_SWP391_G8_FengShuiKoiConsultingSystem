package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.service.BillService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
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
    @PostMapping("/packages/{packageId}/payments/{paymentId}")
    public ApiResponse<BillDTO> createBillByPaymentAndPackage(
            @RequestBody @Valid BillDTO billRequest,
            @PathVariable Integer packageId,
            @PathVariable Integer paymentId) {
        return ApiResponse.<BillDTO>builder()
                .result(billService.createBillByPaymentAndPackage(billRequest, packageId, paymentId))
                .build();
    }

}
