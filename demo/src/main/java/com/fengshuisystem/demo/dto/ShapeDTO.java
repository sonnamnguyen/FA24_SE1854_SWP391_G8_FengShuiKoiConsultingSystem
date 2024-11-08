package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Status;
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
public class ShapeDTO {
    Integer id;
    DestinyDTO destiny;
    @NotBlank(message = "Shape is required")
    @Size(min = 3, max = 50, message = "Shape must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Shape must only contain letters and spaces") // Chỉ cho phép chữ cái và khoảng trắng
   String shape;
    Status status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
}