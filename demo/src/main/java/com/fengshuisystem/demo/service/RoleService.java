package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.request.RoleRequest;
import com.fengshuisystem.demo.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    void delete(Integer role);
}