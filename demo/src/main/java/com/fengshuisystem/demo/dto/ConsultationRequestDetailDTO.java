package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationRequestDetailDTO {
    Integer id;
    // Chỉ cần lưu ID thay vì object hoàn chỉnh của các entity liên quan
    Long animalCategoryId;
    Long shelterCategoryId;

    Double price;
    ConsultationResultDTO requestDetail;
    ShelterCategoryDTO shelterCategory;
    AnimalCategoryDTO animalCategory;
    String description;
    String status;
    Instant createdDate;
    String createdBy;
    Instant updatetedDate;
    String updatetedBy;
    List<ConsultationResultDTO> consultationResults;
}
