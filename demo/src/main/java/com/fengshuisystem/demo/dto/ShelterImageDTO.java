package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.ShelterCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShelterImageDTO {
    private Integer id;
    private ShelterCategoryDTO shelterCategory;
    private String imageUrl;
    private String status;
    private Instant createdDate;
    private String createdBy;
    private Instant updatetedDate;
    private String updatetedBy;
}
