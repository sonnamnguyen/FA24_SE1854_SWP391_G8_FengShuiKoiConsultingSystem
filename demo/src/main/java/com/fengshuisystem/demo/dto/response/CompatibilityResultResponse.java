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
    private String yourDestiny;
    private Double directionScore;
    private List<String> directionsAdvice;
    private String directionExplanation;
    private Double shapeScore;
    private List<String> shapesAdvice;
    private String shapeExplanation;
    private Double numberScore;
    private List<Integer> numbersAdvice;
    private String numberExplanation;
    private Double animalScore;
    private List<AnimalCompatibilityResponse> animalCompatibilityResponse;
    private List<String> animalAdvice;
}
