package com.fengshuisystem.demo.controller;
import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.PackageDTO;
import com.fengshuisystem.demo.service.PackageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PackageController {
    PackageService packageService;
    @PostMapping
    public ApiResponse<PackageDTO> createPackage(@RequestBody PackageDTO packageRequest) {
        return ApiResponse.<PackageDTO>builder()
                .result(packageService.createPackage(packageRequest))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<PackageDTO> updatePackage(@PathVariable Integer id, @RequestBody @Valid PackageDTO packageRequest) {
        return ApiResponse.<PackageDTO>builder()
                .result(packageService.updatePackage(id, packageRequest))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePackage(@PathVariable Integer id) {
        packageService.deletePackage(id);
        return ApiResponse.<String>builder()
                .result("The package has been deleted")
                .build();
    }
}
