package com.fengshuisystem.demo.dto.request;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;


import com.fengshuisystem.demo.validator.DobConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    String fullName;
    @Email(message = "INVALID_EMAIL")
    String email;
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "INVALID_PHONE_NUMBER")
    String phoneNumber;
    @Pattern(regexp = "male|female", message = "INVALID_GENDER")
    String gender;
    String avatar;
    @DobConstraint(min = 2, message = "INVALID_DOB")
    LocalDate dob;
    String code;
    String status;
    Instant createDate;
    String createdBy;
    Instant updateDate;
    String updatedBy;

}
