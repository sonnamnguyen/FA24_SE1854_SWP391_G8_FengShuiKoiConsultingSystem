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
public class ConsultationShelterDTO {
  Integer id;
  Integer consultationId;
  Integer shelterCategoryId;
  List<DirectionDTO> direction;
  String description;
  String status;
  Instant createdDate;
  String createdBy;
  Instant updatetedDate;
  String updatetedBy;
}
