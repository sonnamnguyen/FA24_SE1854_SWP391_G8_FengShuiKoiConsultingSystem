package com.fengshuisystem.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorDTO {
    Integer id;
    DestinyDTO destiny;
    @NotBlank(message = "Color is required")
    @Size(min = 3, max = 30, message = "Color must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Color must only contain letters and spaces") // Kiểm tra chỉ cho phép chữ cái và khoảng trắng
    private String color;
    String status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
}