package com.fengshuisystem.demo.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationAnimalDTO {
  Integer id;
  @JsonIgnore
  Integer consultationResultId;
  @JsonIgnore
  Integer animalCategoryId;
  String description;
  List<NumberDTO> numbers;
  String status;
  Instant createdDate;
  String createdBy;
  Instant updatetedDate;
  String updatetedBy;
}