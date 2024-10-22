
package com.fengshuisystem.demo.dto.request;

import com.fengshuisystem.demo.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @Email(message = "INVALID_EMAIL")
    String email;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    @Size(min = 10, message = "INVALID_PHONE_NUMBER")
    String phoneNumber;


    @Pattern(regexp = "male|female", message = "INVALID_GENDER")
    String gender;

}
