package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.service.PackageService;
import com.fengshuisystem.demo.entity.Package;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/packages")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PackageController {
    PackageService packageService;

    // Lấy tất cả các gói
    @GetMapping
    public ResponseEntity<List<Package>> getAllPackages() {
        List<Package> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages); // Trả về mã trạng thái 200 (OK)
    }

    // Lấy gói theo ID
    @GetMapping("/{id}")
    public ApiResponse<Package> getPackageById(@PathVariable Integer id) {
        return ApiResponse.<Package>builder()
                .result(packageService.getPackageById(id))
                .build();
    }

    // Tạo mới gói
    @PostMapping
    public ResponseEntity<Package> createPackage(@RequestBody Package pkg) {
        Package createdPackage = packageService.createPackage(pkg);
        return ResponseEntity.status(201).body(createdPackage); // Trả về mã trạng thái 201 (Created)
    }

    // Cập nhật gói theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Package> updatePackage(@PathVariable Integer id, @RequestBody Package pkgDetails) {
        Package updatedPackage = packageService.updatePackage(id, pkgDetails);
        return ResponseEntity.ok(updatedPackage); // Trả về mã trạng thái 200 (OK)
    }

    // Xóa gói theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Integer id) {
            packageService.deletePackage(id);
            return ResponseEntity.noContent().build(); // Trả về mã trạng thái 204 (No Content)

    }
}
