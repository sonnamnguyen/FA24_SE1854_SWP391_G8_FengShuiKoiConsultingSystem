package com.fengshuisystem.demo.dto;


import com.fengshuisystem.demo.entity.enums.Request;
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

    // Sử dụng ID thay vì đối tượng để tránh vòng lặp
    Integer consultationRequestId;
    Integer consultationRequestDetailId;

    Integer accountId;
    Integer consultationCategoryId;

    Instant consultationDate;
    String consultantName;
    Request status;
    String description;

    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    List<ConsultationAnimalDTO> consultationAnimals;
    List<ConsultationShelterDTO> consultationShelters;
}
