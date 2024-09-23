package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.constant.PredefinedRole;
import com.fengshuisystem.demo.dto.reponse.UserResponse;
import com.fengshuisystem.demo.dto.request.PasswordCreationRequest;
import com.fengshuisystem.demo.dto.request.UserCreationRequest;
import com.fengshuisystem.demo.dto.request.UserUpdateRequest;
import com.fengshuisystem.demo.entity.Role;
import com.fengshuisystem.demo.entity.User;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.UserMapper;
import com.fengshuisystem.demo.repository.RoleRepository;
import com.fengshuisystem.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreateDate(LocalDate.now());
        log.info("UserCreationRequest: {}", request);


        HashSet<Role> roles = new HashSet<>();
        roleRepository.findByName(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        log.info("Mapped User: {}", user);
        return userMapper.toUserResponse(userRepository.saveAndFlush(user));
    }
    public void createPassword(PasswordCreationRequest request){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

        return userResponse;
    }

//    @PostAuthorize("returnObject.username == authentication.name")
//    public UserResponse updateUser(Integer userId, UserUpdateRequest request) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        userMapper.updateUser(user, request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        List<String> roleNames = request.getRoles();
//        Set<Role> roles = roleRepository.findByName(roleNames);
//        user.setRoles(roles);
//
//        return userMapper.toUserResponse(userRepository.save(user));
//    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(Integer id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

}