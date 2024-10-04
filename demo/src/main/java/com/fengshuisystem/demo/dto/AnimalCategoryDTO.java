package com.fengshuisystem.demo.dto;



import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimalCategoryDTO {
    Integer id;
    String animalCategoryName;
    String discription;
    String origin;
    String status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
    List<AnimalImageDTO> animalImages;
    List<ConsultationAnimalDTO> consultationAnimalDTOList;
    List<ConsultationRequestDetailDTO> consultationRequestDetailList;
    List<ColorDTO> colors;
}
