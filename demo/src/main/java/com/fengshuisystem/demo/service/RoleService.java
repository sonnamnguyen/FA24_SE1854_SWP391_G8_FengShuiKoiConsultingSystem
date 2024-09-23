package com.fengshuisystem.demo.service;

import java.util.HashSet;
import java.util.List;


import com.fengshuisystem.demo.dto.reponse.RoleResponse;
import com.fengshuisystem.demo.dto.request.RoleRequest;
import com.fengshuisystem.demo.mapper.RoleMapper;
import com.fengshuisystem.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(Integer role) {
        roleRepository.deleteById(role);
    }
}
