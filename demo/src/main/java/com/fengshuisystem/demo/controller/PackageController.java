package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageRepository packageRepository;

    // Lấy danh sách tất cả các gói tư vấn
    @GetMapping
    public ResponseEntity<List<Package>> getAllPackages() {
        List<Package> packages = packageRepository.findAll();
        return ResponseEntity.ok(packages);
    }

    // Cho phép người dùng chọn gói tư vấn
    @PostMapping("/choose")
    public ResponseEntity<String> choosePackage(@RequestParam Integer packageId) {
        // Logic lưu gói tư vấn đã chọn, có thể lưu trong session hoặc cơ sở dữ liệu
        return ResponseEntity.ok("Gói tư vấn đã được chọn: " + packageId);
    }
}
