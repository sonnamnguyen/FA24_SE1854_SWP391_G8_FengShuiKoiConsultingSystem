package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DestinyDTO {
    private Integer id;
    private String destiny;
    private List<ColorDTO> colors;
    private List<DirectionDTO> directions;
    private List<NumberDTO> numbers;
 //   private Set<PostDTO> posts;
    private List<ShapeDTO> shapes;
}
