package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.entity.ShelterCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShelterCategoryMapper {
    ShelterCategory toEntity(ShelterCategoryDTO dto);
}
