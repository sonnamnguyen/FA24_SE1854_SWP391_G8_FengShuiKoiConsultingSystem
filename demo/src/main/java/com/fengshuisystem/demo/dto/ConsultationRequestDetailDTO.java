package com.fengshuisystem.demo.dto;

import lombok.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationRequestDetailDTO {
    private Integer id;
    private String description;
    private String status;
    private List<Integer> shelterCategoryIds;
    private List<Integer> animalCategoryIds;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;
}
