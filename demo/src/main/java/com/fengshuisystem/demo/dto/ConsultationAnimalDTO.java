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
  Integer consultationResultId;
  Integer animalCategoryId;
  String description;
  Integer numberId;
  String status;
  Instant createdDate;
  String createdBy;
  Instant updatetedDate;
  String updatetedBy;
}
