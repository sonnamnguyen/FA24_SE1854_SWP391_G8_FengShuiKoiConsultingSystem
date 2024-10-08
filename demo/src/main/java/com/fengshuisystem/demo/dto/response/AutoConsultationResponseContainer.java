package com.fengshuisystem.demo.dto.response;

import com.fengshuisystem.demo.dto.AutoConsultationResponseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoConsultationResponseContainer {
     AutoConsultationResponseDTO consultation1;
     AutoConsultationResponseDTO consultation2;
}
