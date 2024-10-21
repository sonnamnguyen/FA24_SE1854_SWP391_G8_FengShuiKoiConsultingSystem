package com.fengshuisystem.demo.dto.response;


import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;


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
    // fullName -> fullname
    String fullname;
    String email;
    String phoneNumber;
    String gender;
    String avatar;
    LocalDate dob;
    String code;
    String status;
    // creat -> created
    Instant createdDate;
    String createdBy;
    // update -> updated
    Instant updatedDate;
    String updatedBy;
    Boolean noPassword;
    Set<RoleResponse> roles;
}