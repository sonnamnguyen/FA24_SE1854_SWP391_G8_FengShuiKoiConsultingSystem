package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.constant.PredefinedRole;
import com.fengshuisystem.demo.dto.ApiResponse;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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
    @Override
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
@Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

        return userResponse;
    }
@Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(Integer userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
@Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
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


    public ApiResponse<User> activeAccount(String email, String code) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (user.isStatus()) {
            return ApiResponse.<User>builder()
                    .message("Your Account is active")
                    .build();
        }
        if (code.equals(user.getCode())) {
            user.setStatus(true);
            userRepository.save(user);
            return ApiResponse.<User>builder()
                    .message("Active account is succefully!!")
                    .build();
        } else {
            return ApiResponse.<User>builder()
                    .message("Active account is failed!!")
                    .build();
        }
    }

}