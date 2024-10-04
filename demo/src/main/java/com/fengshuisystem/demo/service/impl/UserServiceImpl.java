package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.constant.PredefinedRole;
import com.fengshuisystem.demo.dto.ApiResponse;

import com.fengshuisystem.demo.dto.request.PasswordCreationRequest;
import com.fengshuisystem.demo.dto.request.UserCreationRequest;
import com.fengshuisystem.demo.dto.request.UserUpdateRequest;
import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.Role;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.UserMapper;
import com.fengshuisystem.demo.repository.RoleRepository;
import com.fengshuisystem.demo.repository.UserRepository;
import com.fengshuisystem.demo.service.EmailService;
import com.fengshuisystem.demo.service.UserService;
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

import java.time.Instant;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
@Override
//    public UserResponse createUser(UserCreationRequest request) {
//        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);
//
//        User user = userMapper.toUser(request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setCreateDate(LocalDate.now());
//        user.setCode(generateCode());
//        user.setStatus(false);
//        log.info("UserCreationRequest: {}", request);
//
//
//        HashSet<Role> roles = new HashSet<>();
//        roleRepository.findByName(PredefinedRole.USER_ROLE).ifPresent(roles::add);
//
//        user.setRoles(roles);
//        log.info("Mapped User: {}", user);
//        UserResponse userResponse = userMapper.toUserResponse(userRepository.saveAndFlush(user));
//        sendActivateCode(userResponse.getEmail(), userResponse.getCode());
//        return userResponse;
//    }
public UserResponse createUser(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

    Account user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    log.info("UserCreationRequest: {}", request);


    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(PredefinedRole.USER_ROLE)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

    log.info("UserCreationRequest: {}", roles);
    roles.add(userRole);
    user.setRoles(roles);
    user.setStatus(Status.ACTIVE);
    user.setCreatedDate(Instant.now());
    user.setUpdatedDate(Instant.now());
    log.info("Roles User: {}", user.getRoles());

    return userMapper.toUserResponse(userRepository.save(user));
}
    public UserResponse createAdmin(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        Account user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        HashSet<Role> roles = new HashSet<>();
        roleRepository.findByName(PredefinedRole.ADMIN_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setStatus(Status.ACTIVE);
        user.setCreatedDate(Instant.now());
        user.setUpdatedDate(Instant.now());
        return userMapper.toUserResponse(userRepository.saveAndFlush(user));

    }
    @Override
    public void createPassword(PasswordCreationRequest request){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
@Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    log.info("Roles: {}", user.getRoles());

    var userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

        return userResponse;
    }
@Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(Integer userId, UserUpdateRequest request) {
        Account user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
@Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse deleteUser(Integer userId) {
    Account user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    user.setStatus(Status.DELETED);
    return userMapper.toUserResponse(userRepository.save(user));
}
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
@Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(Integer id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
    private String generateCode() {
        return UUID.randomUUID().toString();
    }
    private void sendActivateCode(String email, String code) {

        String subject = "Activate your account at FengShuiConsultingSystem.com";

        String text = "<html><body>";
        text += "Dear User,<br/><br/>";
        text += "Thank you for registering at FengShuiConsultingSystem.com!<br/><br/>";
        text += "Please use the following code to activate your account: <strong>" + code + "</strong><br/><br/>";
        text += "Alternatively, you can activate your account by clicking on the following link:<br/>";

        String url = "http://localhost:3000/activate/" + email + "/" + code;
        text += "<a href=\"" + url + "\">" + url + "</a><br/><br/>";

        text += "If you did not request an account, please ignore this email.<br/><br/>";
        text += "Need help? Feel free to contact our support team at support@fengshuiconsultingsystem.com.<br/>";
        text += "We are here to assist you.<br/><br/>";
        text += "Best regards,<br/> The FengShuiConsultingSystem Support Team";
        text += "</body></html>";

        emailService.sendEmail("phuy61371@gmail.com", email, subject, text);
    }


//    public ApiResponse<Account> activeAccount(String email, String code) {
//        Account account = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        if (accoun) {
//            return ApiResponse.<Account>builder()
//                    .message("Your Account is active")
//                    .build();
//        }
//        if (code.equals(account.getCode())) {
//            account.setStatus(true);
//            userRepository.save(account);
//            return ApiResponse.<Account>builder()
//                    .message("Active account is succefully!!")
//                    .build();
//        } else {
//            return ApiResponse.<Account>builder()
//                    .message("Active account is failed!!")
//                    .build();
//        }
//    }




//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Account account = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        Collection<? extends GrantedAuthority> authorities = rolesToAuthorities(account.getRoles());
//        return new org.springframework.security.core.userdetails.User(
//                account.getUsername(),
//                account.getPassword(),
//                authorities
//        );
//    }

//    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
//                .collect(Collectors.toList());
//    }

}