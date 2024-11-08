package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationResultMapper extends EntityMapper<ConsultationResultDTO, ConsultationResult> {

    @Mapping(target = "consultationRequestId", source = "request.id")
    @Mapping(target = "consultationRequestDetailId", source = "requestDetail.id")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "consultationCategoryId", source = "consultationCategory.id")
    ConsultationResultDTO toDto(ConsultationResult entity);

    @Mapping(target = "request", ignore = true)
    @Mapping(target = "requestDetail", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "consultationCategory", ignore = true)
    ConsultationResult toEntity(ConsultationResultDTO dto);
}
