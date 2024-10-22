
package com.fengshuisystem.demo.dto.request;

import java.time.Instant;
import java.time.LocalDate;




import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String userName;
    String password;
    String fullName;
    String email;
    String phoneNumber;
    String gender;
    String avatar;
    LocalDate dob;
    String code;
    String status;
    Instant createDate;
    String createdBy;
    Instant updateDate;
    String updatedBy;

}
