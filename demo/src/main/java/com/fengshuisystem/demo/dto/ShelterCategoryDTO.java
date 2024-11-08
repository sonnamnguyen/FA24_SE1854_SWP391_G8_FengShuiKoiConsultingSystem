package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShelterCategoryDTO {

    Integer id;
    @NotBlank(message = "Shelter category name is required")
    @Size(min = 3, max = 50, message = "Shelter category name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Shelter category name cannot contain numbers or special characters")
    private String shelterCategoryName;

    private ShapeDTO shape;

    @NotNull(message = "Width is required")
    @Min(value = 1, message = "Width must be greater than 0")
    @Max(value = 20, message = "Width must be less than or equal to 20 meters") // Giới hạn 20m
    private Double width;

    @NotNull(message = "Height is required")
    @Min(value = 1, message = "Height must be greater than 0")
    @Max(value = 10, message = "Height must be less than or equal to 10 meters") // Giới hạn 10m
    private Double height;

    @NotNull(message = "Length is required")
    @Min(value = 1, message = "Length must be greater than 0")
    @Max(value = 50, message = "Length must be less than or equal to 50 meters") // Giới hạn 50m
    private Double length;

    @NotNull(message = "Diameter is required")
    @Min(value = 1, message = "Diameter must be greater than 0")
    @Max(value = 20, message = "Diameter must be less than or equal to 20 meters") // Giới hạn 20m
    private Double diameter;

    @NotNull(message = "Water volume is required")
    @Min(value = 1, message = "Water volume must be greater than 0")
    @Max(value = 10000, message = "Water volume must be less than or equal to 10000 cubic meters") // Giới hạn 10,000m³
    private Double waterVolume;

    @NotBlank(message = "Water filtration system is required")
    @Size(min = 3, max = 100, message = "Water filtration system must be between 3 and 100 characters")
    private String waterFiltrationSystem;

    @Size(max = 200, message = "Description can be up to 200 characters")
    private String description;

    Status status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    List<ShelterImageDTO> shelterImages;
}
