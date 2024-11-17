package com.fengshuisystem.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.entity.enums.Request;
import jakarta.validation.constraints.Size;
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
  Integer consultationResultId;
  Integer animalCategoryId;
  String description;

  @JsonIgnore
  List<NumberDTO> numbers;
  Request status;
  Instant createdDate;
  String createdBy;
  Instant updatedDate;
  String updatedBy;

  public ConsultationAnimalDTO(
          Integer id,
          Integer animalCategoryId,
          String description,
          Request status,
          Instant createdDate,
          String createdBy,
          Instant updatedDate,
          String updatedBy) {
    this.id = id;
    this.animalCategoryId = animalCategoryId;
    this.description = description;
    this.status = status;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.updatedDate = updatedDate;
    this.updatedBy = updatedBy;
  }
}