package com.fengshuisystem.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.enums.Gender;
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
public class ConsultationRequestDTO {

    Integer id;

    String fullName;
    Integer yob;
    Gender gender;
    String email;
    String phone;

    @JsonIgnore
    Account account;

    Integer packageId;

    String description;

    Request status;

    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    @JsonIgnore
    List<ConsultationRequestDetailDTO> consultationRequestDetails;
    @JsonIgnore
    List<ConsultationResultDTO> consultationResults;

    @JsonIgnore
    List<Bill> bills;
}
