package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.constant.PredefinedRole;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.dto.request.PasswordCreationRequest;
import com.fengshuisystem.demo.dto.request.UpdateFCMRequest;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) throw new AppException(ErrorCode.USER_EXISTED);
        Account user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setCode(generateCode());
        user.setStatus(Status.INACTIVE);
        user.setUpdatedDate(Instant.now());
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(PredefinedRole.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);
        UserResponse userResponse = userMapper.toUserResponse(userRepository.saveAndFlush(user));
        sendActivateCode(userResponse.getEmail(), userResponse.getCode());
        return userResponse;
    }


    public String giveEmailForgotPassword(String email) {
        Account user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        sendForgotCodeToRestPassword(user.getEmail());
        return "Reset Password Successfully";
    }

    public UserResponse createAdmin(UserCreationRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) throw new AppException(ErrorCode.USER_EXISTED);

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

        Account user = userRepository.findByEmail(name).orElseThrow(
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

        Account user = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        log.info("Roles: {}", user.getRoles());

        var userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

        return userResponse;
    }
    @Override
    @PostAuthorize("returnObject.email == authentication.principal.claims['sub']")
    public UserResponse updateUser(String email, UserUpdateRequest request) {
        Account user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public boolean existByUsername(String username) {
        return userRepository.existsByUserName(username);
    }
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
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

    private void sendForgotCodeToRestPassword(String email) {

        String subject = "Reset your password at FengShuiConsultingSystem.com";

        String text = "<html><body>";
        text += "Dear User,<br/><br/>";
        text += "We received a request to reset the password for your account at FengShuiConsultingSystem.com.<br/><br/>";
        text += "Please use the following code to reset your password: <strong>"  + "</strong><br/><br/>";
        text += "Alternatively, you can reset your password by clicking on the following link:<br/>";

        String url = "http://localhost:3000/reset-password/" + email;
        text += "<a href=\"" + url + "\">" + url + "</a><br/><br/>";

        text += "If you did not request a password reset, please ignore this email or contact support.<br/><br/>";
        text += "Need help? Feel free to contact our support team at support@fengshuiconsultingsystem.com.<br/>";
        text += "We are here to assist you.<br/><br/>";
        text += "Best regards,<br/> The FengShuiConsultingSystem Support Team";
        text += "</body></html>";

        emailService.sendEmail("phuy61371@gmail.com", email, subject, text);
    }
    public String activeAccount(String email, String code) {
        Account account = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (account.getStatus().equals(Status.ACTIVE)) {
            return new AppException(ErrorCode.USER_EXISTED).toString();
        }

        if (code.equals(account.getCode())) {
            account.setStatus(Status.ACTIVE);
            userRepository.save(account);
            return "Activated account successfully";
        } else {
            return new AppException(ErrorCode.ERROR_CODE).toString();
        }
    }
    public String resetPassword(String email, String newPassword) {
        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (account.getStatus().equals(Status.INACTIVE)) {
            throw new AppException(ErrorCode.ERROR_CODE);
        }
        if (account.getStatus().equals(Status.ACTIVE)) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            account.setPassword(encodedPassword);
            userRepository.save(account);
            return "Password reset successfully for account";
        }
        return "Could not reset password. Account status not recognized.";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = userRepository.findAllByStatus(status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(userMapper::toUserResponse).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getUsersBySearch(String name, int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = userRepository.findAllByUserNameContainingOriginContaining(name, status, pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(userMapper::toUserResponse).toList())
                .build();
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse setRole(Integer userId, List<Integer> ids) {
        Account user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<Role> roles = new HashSet<>();
        var userRole = roleRepository.findByIdIn(ids)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.saveAndFlush(user));
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public long getNewUsersToday() {
        return userRepository.countNewUsersToday();
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public long getNewUsersThisWeek() {
        return userRepository.countNewUsersThisWeek();
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public long getNewUsersThisMonth() {
        return userRepository.countNewUsersThisMonth();
    }

    // Phương thức để lưu hoặc cập nhật FCM token cho người dùng đã đăng nhập
    public Account updateFCM(UpdateFCMRequest updateFCMRequest) {
        Account account = getCurrentAccount();

        // Kiểm tra nếu FCM token mới khác với token hiện tại
        if (updateFCMRequest.getFcmToken() != null &&
                !updateFCMRequest.getFcmToken().equals(account.getFcmToken())) {
            account.setFcmToken(updateFCMRequest.getFcmToken());
            return userRepository.save(account);
        }

        return account;
    }

    private Account getCurrentAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

}