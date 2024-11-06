package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.request.RoleRequest;
import com.fengshuisystem.demo.dto.response.RoleResponse;
import com.fengshuisystem.demo.mapper.RoleMapper;
import com.fengshuisystem.demo.repository.RoleRepository;
import com.fengshuisystem.demo.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    @Override
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
    @Override
    public void delete(Integer role) {
        roleRepository.deleteById(role);
    }
}