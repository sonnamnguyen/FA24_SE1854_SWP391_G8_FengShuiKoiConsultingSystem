package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Destiny2DTO {
    Integer id;
    String destiny;
    String code;
    String name;
    Integer minValue;
    Integer maxValue;
    String status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
}
