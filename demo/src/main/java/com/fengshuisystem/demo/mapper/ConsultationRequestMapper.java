package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ConsultationRequestMapper {

    // Ánh xạ từ Entity sang DTO, sử dụng phương thức tùy chỉnh để lấy packageId
    @Mapping(source = "packageId", target = "packageId", qualifiedByName = "packageToPackageId")
    ConsultationRequestDTO toDTO(ConsultationRequest consultationRequest);

    // Ánh xạ từ DTO sang Entity, packageId sẽ được gán trong Service Layer
    @Mapping(target = "packageId", ignore = true)
    ConsultationRequest toEntity(ConsultationRequestDTO consultationRequestDTO);

    // Phương thức ánh xạ tùy chỉnh: từ Package Entity sang packageId
    @Named("packageToPackageId")
    default Integer mapPackageToPackageId(com.fengshuisystem.demo.entity.Package packageEntity) {
        return packageEntity != null ? packageEntity.getId() : null;
    }
}
