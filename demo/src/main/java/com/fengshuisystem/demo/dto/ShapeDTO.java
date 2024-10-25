package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShapeDTO {
    Integer id;
    DestinyDTO destiny;
    String shape;
    Status status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
}