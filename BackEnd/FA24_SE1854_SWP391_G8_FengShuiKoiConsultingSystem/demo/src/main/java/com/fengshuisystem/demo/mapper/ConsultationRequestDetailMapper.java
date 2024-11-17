package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.AnimalCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ConsultationRequestDetailMapper {

    @Mapping(source = "shelterCategories", target = "shelterCategoryIds", qualifiedByName = "shelterCategoryToIds")
    @Mapping(source = "animalCategories", target = "animalCategoryIds", qualifiedByName = "animalCategoryToIds")
    ConsultationRequestDetailDTO toDTO(ConsultationRequestDetail entity);

    @Mapping(target = "shelterCategories", ignore = true)
    @Mapping(target = "animalCategories", ignore = true)
    @Mapping(target = "consultationResults", ignore = true)
    @Mapping(target = "consultationRequest", ignore = true)
    ConsultationRequestDetail toEntity(ConsultationRequestDetailDTO dto);

    void updateEntityFromDTO(ConsultationRequestDetailDTO detailDTO, @MappingTarget ConsultationRequestDetail detail);

    // Custom mapping method for shelterCategoryToIds
    @Named("shelterCategoryToIds")
    default List<Integer> shelterCategoryToIds(Set<ShelterCategory> categories) {
        return categories.stream()
                .map(ShelterCategory::getId) // Assuming getId() returns Integer
                .collect(Collectors.toList());
    }

    // Custom mapping method for animalCategoryToIds
    @Named("animalCategoryToIds")
    default List<Integer> animalCategoryToIds(Set<AnimalCategory> categories) {
        return categories.stream()
                .map(AnimalCategory::getId) // Assuming getId() returns Integer
                .collect(Collectors.toList());
    }
}
