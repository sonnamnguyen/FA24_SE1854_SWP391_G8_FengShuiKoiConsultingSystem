package com.fengshuisystem.demo.mapper;


import com.fengshuisystem.demo.dto.request.RoleRequest;
import com.fengshuisystem.demo.dto.response.RoleResponse;
import com.fengshuisystem.demo.entity.Role;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}