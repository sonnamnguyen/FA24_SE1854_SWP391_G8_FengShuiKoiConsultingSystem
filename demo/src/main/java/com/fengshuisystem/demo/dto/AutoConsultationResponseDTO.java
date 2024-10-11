package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoConsultationResponseDTO {
     String message;
     List<Integer> numbers;
     List<String> directions;
     List<String> colors;
     List<String> shapes;
     List<String> shelters;
     List<String> animals;
}
