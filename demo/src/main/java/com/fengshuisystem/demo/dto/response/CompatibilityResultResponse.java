package com.fengshuisystem.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompatibilityResultResponse {
    String yourDestiny;
    double directionScore;
    String directionExplanation;
    double shapeScore;
    String shapeExplanation;
    double numberScore;
    String numberExplanation;
    List<AnimalCompatibilityResponse> animalCompatibilityResponse;
}
