package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationCategoryDTO {
    Integer id;
    String name;
    Status status;
}
