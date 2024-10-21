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
    ConsultationResultDTO requestDetail;

    ShelterCategoryDTO shelterCategory;
    AnimalCategoryDTO animalCategory;

    // bo sung dang List
    List<ShelterCategoryDTO> shelterCategoryies;
    List<AnimalCategoryDTO> animalCategories;

    // add description
    String description;

    String status;
    Instant createdDate;
    String createdBy;
    Instant updatetedDate;
    String updatetedBy;
    List<ConsultationResultDTO> consultationResults;
}
