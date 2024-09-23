package com.fengshuisystem.demo.controller;

import java.util.List;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.reponse.RoleResponse;
import com.fengshuisystem.demo.dto.request.RoleRequest;
import com.fengshuisystem.demo.service.RoleService;
import org.springframework.web.bind.annotation.*;



import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable Integer role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }
}