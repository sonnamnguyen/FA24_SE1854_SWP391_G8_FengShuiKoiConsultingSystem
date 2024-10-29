package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Status;
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
    String animalCategoryName;
    String description;
    String origin;

    Status status;

    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    List<AnimalImageDTO> animalImages;

    List<ColorDTO> colors;
}
