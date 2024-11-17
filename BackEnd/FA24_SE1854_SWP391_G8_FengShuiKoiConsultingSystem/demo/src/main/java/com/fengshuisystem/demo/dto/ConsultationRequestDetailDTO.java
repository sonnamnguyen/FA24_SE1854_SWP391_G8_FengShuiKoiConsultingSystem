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
public class ConsultationRequestDetailDTO {

    Integer id;

    private List<Integer> shelterCategoryIds;
    private List<Integer> animalCategoryIds;

    String description;

    Request status;

    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    List<ConsultationResultDTO> consultationResults;
}
