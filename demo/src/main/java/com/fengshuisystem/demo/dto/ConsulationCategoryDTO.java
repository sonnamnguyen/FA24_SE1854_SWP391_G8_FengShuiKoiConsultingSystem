package com.fengshuisystem.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsulationCategoryDTO {
    Integer id;
    String name;
    String status;
    List<ConsultationResultDTO> consulationResults;
}
