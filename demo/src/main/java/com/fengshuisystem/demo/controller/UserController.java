package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.request.PasswordCreationRequest;
import com.fengshuisystem.demo.dto.request.UserCreationRequest;
import com.fengshuisystem.demo.dto.request.UserUpdateRequest;
import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserServiceImpl userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostMapping("/admin")
    public ApiResponse<UserResponse> createAdmin(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createAdmin(request))
                .build();
    }

    @PostMapping("/create-password")
   ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        userService.createPassword(request);
        return ApiResponse.<Void>builder()
                .message("Password has been created, you could use it to log-in")
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
     ApiResponse<UserResponse> getUser(@PathVariable("userId") Integer userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<UserResponse> deleteUser(@PathVariable Integer userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.deleteUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String email, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(email, request))
                .build();
    }

    @GetMapping("/existByUserName")
    ApiResponse<Boolean> existByUserName(@RequestParam String userName) {
        return ApiResponse.<Boolean>builder()
                .result(userService.existByUsername(userName))
                .build();
    }

    @GetMapping("/existByEmail")
    ApiResponse<Boolean> existByEmail(@RequestParam String email) {
        return ApiResponse.<Boolean>builder()
                .result(userService.existByEmail(email))
                .build();
    }
        @GetMapping("/activate")
    public ApiResponse<String> register(@RequestParam String email, @RequestParam String code) {
            return ApiResponse.<String>builder()
                    .result(userService.activeAccount(email, code))
                    .build();
    }
    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestParam String email) {
        return ApiResponse.<String>builder()
                .result(userService.giveEmailForgotPassword(email))
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            @RequestParam String email,
            @RequestBody PasswordCreationRequest newPassword) {

        return ApiResponse.<String>builder()
                .result(userService.resetPassword(email, newPassword.getPassword()))
                .build();
    }
}