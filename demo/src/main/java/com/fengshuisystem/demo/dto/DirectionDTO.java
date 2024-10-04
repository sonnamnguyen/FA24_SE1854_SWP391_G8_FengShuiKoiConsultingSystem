package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectionDTO {
    private Integer id;
    private String direction;
    private DestinyDTO destinyDTO;
    private List<ConsultationShelterDTO> consultationShelters;
}
