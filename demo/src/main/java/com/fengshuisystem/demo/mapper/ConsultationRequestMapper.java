package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.ConsultationRequestDTO;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.Package;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ConsultationRequestMapper {

    @Mapping(source = "packageId", target = "packageId", qualifiedByName = "packageToPackageId")
    ConsultationRequestDTO toDTO(ConsultationRequest consultationRequest);

    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "packageId", ignore = true) // Updated to "packageId" instead of "package"
    @Mapping(source = "yob", target = "yob")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    ConsultationRequest toEntity(ConsultationRequestDTO consultationRequestDTO);

    // Custom mapping method to convert the package to package ID
    @Named("packageToPackageId")
    default Integer packageToPackageId(Package packageObj) {
        return packageObj != null ? packageObj.getId() : null; // Assuming getId() returns Integer
    }
}
