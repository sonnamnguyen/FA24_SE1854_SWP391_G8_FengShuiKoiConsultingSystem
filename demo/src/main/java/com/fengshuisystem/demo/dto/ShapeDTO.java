package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.Destiny;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShapeDTO {
    Integer id;
    private Destiny destiny;
    private String shape;
    private String status;
    private Instant createdDate;
    private String createdBy;
    private Instant updatedDate;
    private String updatedBy;
    private List<ShelterCategoryDTO> shelterCategories;
}
