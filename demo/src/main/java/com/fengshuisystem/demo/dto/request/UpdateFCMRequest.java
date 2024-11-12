package com.fengshuisystem.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateFCMRequest {
    @NotNull(message = "FCM token must not be null")
    private String fcmToken;
}
