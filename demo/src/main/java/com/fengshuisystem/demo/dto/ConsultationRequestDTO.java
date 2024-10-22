package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.dto.response.UserResponse;
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
    UserResponse account;
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