package com.fengshuisystem.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DestinyRequest {
    Integer directionId;
    String directionName;
    Integer shapeId;
    String shapeName;
    Integer numberId;
    Integer numberName;
    List<AnimalRequest> animal;
}
