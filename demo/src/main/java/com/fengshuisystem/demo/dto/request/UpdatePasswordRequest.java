package com.fengshuisystem.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePasswordRequest {
   String currentPassword;
     String newPassword;


}
