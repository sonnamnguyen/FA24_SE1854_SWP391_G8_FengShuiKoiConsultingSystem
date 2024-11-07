package com.fengshuisystem.demo.dto.response;


import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;


import com.fengshuisystem.demo.entity.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String userName;
    String password;
    String fullName;
    String email;
    String phoneNumber;

    Gender gender;

    String avatar;
    LocalDate dob;
    String code;
    String status;
    Instant createDate;
    String createdBy;
    Instant updateDate;
    String updatedBy;
    Boolean noPassword;
    Set<RoleResponse> roles;
}