package com.fengshuisystem.demo.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompatibilityResultResponse {
    private String yourDestiny;
    private Double directionScore;
    private String directionExplanation;
    private Set<String> directionsAdvice;
    private Double shapeScore;
    private String shapeExplanation;
    private Set<String> shapesAdvice;
    private Double numberScore;
    private String numberExplanation;
    private Set<Integer> numbersAdvice;
    private Double animalScore;
    private List<AnimalCompatibilityResponse> animalCompatibilityResponse;
    private Set<String> animalAdvice;
}
