package com.fengshuisystem.demo.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimalCategoryDTO {
    Integer id;
    String animalCategoryName;
    String description;
    String origin;
    String status;
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;
    List<AnimalImageDTO> animalImages;
    @JsonIgnore
    List<ConsultationAnimalDTO> consultationAnimals;
    @JsonIgnore
    List<ConsultationRequestDetailDTO> consultationRequestDetails;
    List<ColorDTO> colors;
}
