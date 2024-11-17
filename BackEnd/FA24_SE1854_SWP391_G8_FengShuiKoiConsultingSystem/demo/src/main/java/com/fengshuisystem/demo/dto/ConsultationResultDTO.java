package com.fengshuisystem.demo.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.entity.ConsultationCategory;
import com.fengshuisystem.demo.entity.enums.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationResultDTO {
    Integer id;

    Integer consultationRequestId;
    Integer consultationRequestDetailId;

    Integer accountId;

    @JsonIgnore
    ConsultationCategory consultationCategory;

    Integer consultationCategoryId;

    Instant consultationDate;
    String consultantName;
    Request status;

    @NotBlank
    String description;

    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    List<ConsultationAnimalDTO> consultationAnimals;
    List<ConsultationShelterDTO> consultationShelters;
}
