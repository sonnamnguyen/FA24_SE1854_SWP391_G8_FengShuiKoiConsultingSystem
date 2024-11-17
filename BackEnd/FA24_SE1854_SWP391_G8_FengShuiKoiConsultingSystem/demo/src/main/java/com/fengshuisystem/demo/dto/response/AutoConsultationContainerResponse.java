package com.fengshuisystem.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoConsultationContainerResponse {
    String destiny;
    String destinyTuongSinh;
    AutoConsultationResponse consultation1;
    AutoConsultationResponse consultation2;
}