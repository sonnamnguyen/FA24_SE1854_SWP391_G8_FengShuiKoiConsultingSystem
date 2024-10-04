package com.fengshuisystem.demo.dto;



import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NumberDTO {
    private DestinyDTO destiny;
    private Integer number;
    private List<ConsultationAnimalDTO> consultationAnimals;
}
