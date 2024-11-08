package com.fengshuisystem.demo.dto.response;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.ShelterCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoConsultationResponse {
    List<Integer> numbers;
    List<String> directions;
    List<String> colors;
    List<String> shapes;
    List<ShelterCategoryDTO> shelters;
    List<AnimalCategoryDTO> animals;
}
