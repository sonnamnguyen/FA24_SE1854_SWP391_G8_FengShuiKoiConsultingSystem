package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimalCategoryDTO {
    Integer id;
    @NotBlank(message = "Animal category name is required")
    @Size(min = 3, max = 50, message = "Animal category name must be between 3 and 50 characters")
    private String animalCategoryName;
    @Size(max = 200, message = "Description can be up to 200 characters")
    private String description;
    @NotBlank(message = "Origin is required")
    @Size(min = 2, max = 30, message = "Origin must be between 2 and 30 characters")
    private String origin;
    Status status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
    List<AnimalImageDTO> animalImages;
    List<ColorDTO> colors;
}
