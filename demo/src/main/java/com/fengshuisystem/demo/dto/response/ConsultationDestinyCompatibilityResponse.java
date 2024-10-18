package com.fengshuisystem.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationDestinyCompatibilityResponse {
    String huong;
    String huongScore;
    String so;
    String soScore;
    String mau;
    String mauScore;
}
