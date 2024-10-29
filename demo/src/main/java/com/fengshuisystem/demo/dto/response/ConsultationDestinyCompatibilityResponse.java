package com.fengshuisystem.demo.dto.response;

import com.fengshuisystem.demo.dto.response.ColorResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationDestinyCompatibilityResponse {
    String direction;
    Integer directionScore;
    String quantity;
    Integer quantityScore;
    List<ColorResponse> color;
    double colorAverageScore;
    double totalScore;
}
