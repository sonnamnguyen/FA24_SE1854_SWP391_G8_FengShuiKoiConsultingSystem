package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.entity.ConsultationResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationResultMapper {

    @Mapping(target = "consultationRequestId", source = "request.id")
    @Mapping(target = "consultationRequestDetailId", source = "requestDetail.id")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "consultationCategoryId", source = "consultationCategory.id")
    ConsultationResultDTO toDto(ConsultationResult entity);

    @Mapping(target = "request.id", source = "consultationRequestId")
    @Mapping(target = "requestDetail.id", source = "consultationRequestDetailId")
    @Mapping(target = "account.id", source = "accountId")
    @Mapping(target = "consultationCategory.id", source = "consultationCategoryId")
    ConsultationResult toEntity(ConsultationResultDTO dto);
}
