package com.fengshuisystem.demo.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationAnimalDTO {
  Integer id;
  ConsultationResultDTO consultation;
  AnimalCategoryDTO animalCategory;
  String description;
  NumberDTO number;
  String status;
  Instant createdDate;
  String createdBy;
  Instant updatetedDate;
  String updatetedBy;
}
