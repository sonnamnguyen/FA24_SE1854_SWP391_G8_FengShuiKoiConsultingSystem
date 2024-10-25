package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import com.fengshuisystem.demo.entity.ShelterCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ConsultationRequestDetailMapper {

    // Mapping from Entity to DTO, including custom mapping for categories
    @Mapping(source = "shelterCategories", target = "shelterCategoryIds", qualifiedByName = "shelterCategoryToIds")
    @Mapping(source = "animalCategories", target = "animalCategoryIds", qualifiedByName = "animalCategoryToIds")
    ConsultationRequestDetailDTO toDTO(ConsultationRequestDetail entity);

    // Mapping from DTO to Entity, ignoring relationships handled in the service layer
    @Mapping(target = "shelterCategories", ignore = true)
    @Mapping(target = "animalCategories", ignore = true)
    @Mapping(target = "consultationResults", ignore = true)
    @Mapping(target = "consultationRequest", ignore = true)
    ConsultationRequestDetail toEntity(ConsultationRequestDetailDTO dto);

    // Custom method to map a set of ShelterCategory entities to a list of their IDs
    @Named("shelterCategoryToIds")
    default List<Integer> mapShelterCategoriesToIds(Set<ShelterCategory> shelterCategories) {
        return shelterCategories.stream()
                .map(com.fengshuisystem.demo.entity.ShelterCategory::getId)
                .toList();
    }

    // Custom method to map a set of AnimalCategory entities to a list of their IDs
    @Named("animalCategoryToIds")
    default List<Integer> mapAnimalCategoriesToIds(Set<com.fengshuisystem.demo.entity.AnimalCategory> animalCategories) {
        return animalCategories.stream()
                .map(com.fengshuisystem.demo.entity.AnimalCategory::getId)
                .toList();
    }

    // Phương thức để cập nhật dữ liệu từ DTO vào entity
    void updateEntityFromDTO(ConsultationRequestDetailDTO detailDTO, @MappingTarget ConsultationRequestDetail detail);
}
