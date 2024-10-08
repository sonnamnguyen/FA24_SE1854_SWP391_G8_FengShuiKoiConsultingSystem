package com.fengshuisystem.demo.dto.reponse;

import java.time.LocalDate;
import java.util.Set;

import com.fengshuisystem.demo.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String password;
    String email;
    LocalDate dob;
    String phoneNumber;
    String avatar;
    String gender;
    String code;
    Boolean status;
    LocalDate createDate;
    Boolean noPassword;
    Set<RoleResponse> roles;


}