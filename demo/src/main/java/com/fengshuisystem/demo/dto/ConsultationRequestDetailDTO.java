package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationRequestDetailDTO {
    Integer id;
    ConsultationResultDTO requestDetailId;
    ShelterCategoryDTO shelterCategoryId;
    AnimalCategoryDTO animalCategoryId;
    String status;
    Instant createdDate;
    String createdBy;
    Instant updatetedDate;
    String updatetedBy;
    List<ConsultationResultDTO> consultationResults;
}
