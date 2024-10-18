package com.fengshuisystem.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationDestinyCompatibilityRequest {
    String huongName;
    String mauName;
    Integer soName;
}
