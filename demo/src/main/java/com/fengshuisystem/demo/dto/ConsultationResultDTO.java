package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.dto.request.UserCreationRequest;
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
    UserCreationRequest account;
    ConsultationCategory consultationCategory;
    Integer consultationAnimalId;
    Integer consultationShelterId;
    Instant consultationDate;
    String consultantName;
    String status ;
    Instant createdDate;
    String createdBy;
    Instant updatetedDate;
    String updatetedBy;
    List<ConsultationAnimalDTO> consultationAnimals ;
    List<ConsultationShelterDTO> consultationShelters;
}
