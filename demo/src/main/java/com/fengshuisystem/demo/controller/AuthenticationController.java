package com.fengshuisystem.demo.controller;

import java.text.ParseException;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.reponse.AuthenticationResponse;
import com.fengshuisystem.demo.dto.reponse.IntrospectResponse;
import com.fengshuisystem.demo.dto.request.AuthenticationRequest;
import com.fengshuisystem.demo.dto.request.IntrospectRequest;
import com.fengshuisystem.demo.dto.request.LogoutRequest;
import com.fengshuisystem.demo.dto.request.RefreshRequest;
import com.fengshuisystem.demo.service.AuthenticationService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated // Bổ sung tính năng kiểm tra tính hợp lệ của arg

public class AuthenticationController {
    AuthenticationService authenticationService;

    // API xác thực thông qua mã code (outbound)
    @PostMapping("/outbound/authentication")
    ApiResponse<AuthenticationResponse> outboundAuthenticate(
            // Thêm @NotBlank
            @RequestParam("code") @NotBlank(message = "Code cannot empty") String code
    ){
        var result = authenticationService.outboundAuthenticate(code);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    // API xác thực (log in) thông thường với request body
    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    // API introspect token, kiểm tra token có hợp lệ không
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    // API refresh token khi token cũ hết hạn
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    // API logout
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}