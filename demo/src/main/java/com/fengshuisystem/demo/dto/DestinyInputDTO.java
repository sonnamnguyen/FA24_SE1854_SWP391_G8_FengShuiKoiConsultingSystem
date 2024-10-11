package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DestinyInputDTO {
     Integer directionId;
     String directionName;
     Integer colorId;
     String colorName;
     Integer shapeId;
     String shapeName;
     Integer numberId;
     Integer numberName;
     List<AnimalInputDTO> animal;
}
