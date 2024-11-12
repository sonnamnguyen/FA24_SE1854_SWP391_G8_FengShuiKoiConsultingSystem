package com.fengshuisystem.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.entity.enums.Request;
import jakarta.validation.constraints.NotNull;
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
public class ConsultationShelterDTO {

    Integer id;

    Integer consultationResultId;

    Integer shelterCategoryId;

    @JsonIgnore
    List<DirectionDTO> direction;
    String description;
    Request status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
}
