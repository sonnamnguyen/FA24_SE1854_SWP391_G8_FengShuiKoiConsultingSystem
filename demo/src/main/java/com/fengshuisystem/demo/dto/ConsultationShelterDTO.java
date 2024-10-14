package com.fengshuisystem.demo.dto;



import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationShelterDTO {
  Integer id;
  ConsultationResultDTO consultation;
  ShelterCategoryDTO shelterCategory;
  DirectionDTO direction;
  String description;
  String status;
  Instant createdDate;
  String createdBy;
  Instant updatetedDate;
  String updatetedBy;
}
