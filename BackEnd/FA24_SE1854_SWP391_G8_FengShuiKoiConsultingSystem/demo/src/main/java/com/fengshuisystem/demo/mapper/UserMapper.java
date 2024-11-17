package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.request.UserCreationRequest;
import com.fengshuisystem.demo.dto.request.UserUpdateRequest;
import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Account toUser(UserCreationRequest request);

    UserResponse toUserResponse(Account user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget Account user, UserUpdateRequest request);
}