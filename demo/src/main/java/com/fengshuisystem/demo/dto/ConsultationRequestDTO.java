package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.entity.Account;
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
//    UserResponse account;
    Account account;
    Integer packageId;
    String description;
    String status;
    Instant createdDate;
    String createdBy;
    // updateted -> updated
    Instant updatedDate;
    String updatedBy;
    List<ConsultationRequestDetailDTO> consultationRequestDetails;
    List<ConsultationResultDTO> consultationResults;
}