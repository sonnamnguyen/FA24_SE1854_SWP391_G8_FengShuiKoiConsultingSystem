package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DestinyDTO {
    Integer id;
     String destiny;
     List<ColorDTO> colors;
    List<DirectionDTO> directions;
     List<NumberDTO> numbers;
 //   private Set<PostDTO> posts;
     List<ShapeDTO> shapes;
}
