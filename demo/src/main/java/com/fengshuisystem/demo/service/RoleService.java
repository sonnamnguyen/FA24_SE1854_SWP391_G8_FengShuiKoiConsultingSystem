package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.request.RoleRequest;
import com.fengshuisystem.demo.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    public RoleResponse create(RoleRequest request);
    public List<RoleResponse> getAll();
    public void delete(Integer role);
}
