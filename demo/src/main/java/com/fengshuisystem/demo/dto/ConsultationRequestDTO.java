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
public class ConsultationRequestDTO {
     Integer id;
     Integer accountId;
     Integer packageId;
    String description;
     String status;
     Instant createdDate;
     String createdBy;
     Instant updatetedDate;
     String updatetedBy;
     List<ConsultationRequestDetailDTO> consultationRequestDetails;
     List<ConsultationResultDTO> consultationResults;
}
