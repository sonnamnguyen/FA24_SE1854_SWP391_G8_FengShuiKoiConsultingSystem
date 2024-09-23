package com.fengshuisystem.demo.dto.request;

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

    @Email(message = "INVALID_EMAIL")
    String email;

    @DobConstraint(min = 2, message = "INVALID_DOB")
    LocalDate dob;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "INVALID_PHONE_NUMBER")
    String phoneNumber;


    String avatar;

    @Pattern(regexp = "male|female", message = "INVALID_GENDER")
    String gender;

    String code;
    Boolean status;

    LocalDate createDate;

}
