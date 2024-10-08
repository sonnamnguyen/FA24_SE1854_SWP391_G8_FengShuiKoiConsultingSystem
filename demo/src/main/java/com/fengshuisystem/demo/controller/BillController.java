package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.service.BillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bills")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BillController {
    BillService billService;

    // Lấy tất cả các hóa đơn
    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills); // Trả về mã trạng thái 200 (OK)
    }

    // Lấy hóa đơn theo ID
    @GetMapping("/{id}")
    public ApiResponse<Bill> getBillById(@PathVariable Integer id) {
              return ApiResponse.<Bill>builder()
                .result(billService.getBillById(id))
                .build();
    }

    // Tạo mới hóa đơn
    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill createdBill = billService.createBill(bill);
        return ResponseEntity.status(201).body(createdBill); // Trả về mã trạng thái 201 (Created)
    }

    // Cập nhật hóa đơn theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable Integer id, @RequestBody Bill billDetails) {
        try {

            Bill updatedBill = billService.updateBill(id, billDetails);
            return ResponseEntity.ok(updatedBill); // Trả về mã trạng thái 200 (OK)
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Trả về mã trạng thái 404 nếu không tìm thấy
        }
    }

    // Xóa hóa đơn theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Integer id) {
            billService.deleteBill(id);
            return ResponseEntity.noContent().build(); // Trả về mã trạng thái 204 (No Content)

    }
}
