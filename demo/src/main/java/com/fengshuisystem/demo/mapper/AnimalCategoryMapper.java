package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnimalCategoryMapper {
    AnimalCategory toEntity(AnimalCategoryDTO dto);
}
