package com.fengshuisystem.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationDestinyCompatibilityRequest {
    String directionName;
    List<String> colorName;
    Integer quantityName;
}
