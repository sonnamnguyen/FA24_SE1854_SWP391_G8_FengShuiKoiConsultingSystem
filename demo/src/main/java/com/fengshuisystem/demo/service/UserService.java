package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.reponse.UserResponse;
import com.fengshuisystem.demo.dto.request.PasswordCreationRequest;
import com.fengshuisystem.demo.dto.request.UserCreationRequest;
import com.fengshuisystem.demo.dto.request.UserUpdateRequest;

import java.util.List;

public interface UserService {
    public UserResponse createUser(UserCreationRequest request);
    public void createPassword(PasswordCreationRequest request);
    public UserResponse getMyInfo();
    public UserResponse updateUser(Integer userId, UserUpdateRequest request);
    public void deleteUser(Integer userId);
    public List<UserResponse> getUsers();
    public UserResponse getUser(Integer id);
}
