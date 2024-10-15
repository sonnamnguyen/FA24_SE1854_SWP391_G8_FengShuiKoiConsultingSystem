package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.entity.*;
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
    ConsultationRequest request;
    ConsultationRequestDetailDTO requestDetail;
    UserResponse account;
    ConsultationCategory consultationCategory;
    Instant consultationDate;
    String consultantName;
    String status ;
    Instant createdDate;
    String createdBy;
    Instant updatetedDate;
    String updatetedBy;
    List<ConsultationShelterDTO> consultationShelters;
}
