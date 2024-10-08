package com.fengshuisystem.demo.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

import com.fengshuisystem.demo.constant.PredefinedRole;
import com.fengshuisystem.demo.dto.reponse.AuthenticationResponse;
import com.fengshuisystem.demo.dto.reponse.IntrospectResponse;
import com.fengshuisystem.demo.dto.request.*;
import com.fengshuisystem.demo.entity.InvalidatedToken;
import com.fengshuisystem.demo.entity.Role;
import com.fengshuisystem.demo.entity.User;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.repository.InvalidatedTokenRepository;
import com.fengshuisystem.demo.repository.RoleRepository;
import com.fengshuisystem.demo.repository.UserRepository;
import com.fengshuisystem.demo.repository.httpclient.OutboundIdentityClient;
import com.fengshuisystem.demo.repository.httpclient.OutboundUserClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final RoleRepository roleRepository;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    @Value("${outbound.identity.client-id}")
    private String CLIENT_ID;

    @Value("${outbound.identity.client-secret}")
    private String CLIENT_SECRET;

    @Value("${outbound.identity.redirect-uri}")
    private String REDIRECT_URI;

    private static final String GRANT_TYPE = "authorization_code";

    // Kiểm tra tính hợp lệ của token (introspect)
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
            log.error("Token validation failed: {}", e.getMessage());
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    // Tìm kiếm hoặc tạo role
    public Role findOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role newRole = Role.builder().name(roleName).build();
            log.info("Creating new role: {}", roleName);
            return roleRepository.save(newRole);
        });
    }

    // Xác thực qua code từ hệ thống khác (outbound)
    public AuthenticationResponse outboundAuthenticate(String code) {
        if (code == null || code.trim().isEmpty()) {
            log.error("Authorization code is null or empty");
            throw new AppException(ErrorCode.INVALID_REQUEST, "Authorization code cannot be null or empty.");
        }

        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());

        log.info("Received access token from outbound system");

        // Get user info
        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());
        log.info("User Info from outbound system: {}", userInfo);

        Set<Role> roles = new HashSet<>();
        Role userRole = findOrCreateRole(PredefinedRole.USER_ROLE);
        roles.add(userRole);

        // Tạo mới user nếu chưa tồn tại
        var user = userRepository.findByUsername(userInfo.getEmail()).orElseGet(
                () -> {
                    log.info("User not found, creating new user: {}", userInfo.getEmail());
                    return userRepository.save(User.builder()
                            .username(userInfo.getEmail())
                            .roles(roles)
                            .build());
                });

        // Generate token
        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }

    // Xác thực thông thường (local)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Invalid credentials for user: {}", request.getUsername());
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    // Đăng xuất (logout)
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            // Lưu token vào danh sách bị vô hiệu
            InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
            invalidatedTokenRepository.save(invalidatedToken);
            log.info("Token invalidated: {}", jit);
        } catch (AppException exception) {
            log.warn("Token already expired or invalid: {}", exception.getMessage());
        }
    }

    // Refresh token
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    // Tạo JWT token
    private String generateToken(User user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("fengshuikoiconsultingsystem.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user))
                    .build();

            JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating JWT for user {}: {}", user.getUsername(), e.getMessage());
            throw new AppException(ErrorCode.INTERNAL_ERROR, "Unable to generate JWT for user.");
        }
    }

    // Xác minh JWT token
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (isRefresh) {
            expiryTime = Date.from(signedJWT.getJWTClaimsSet().getIssueTime()
                    .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS));
        }

        if (!signedJWT.verify(new MACVerifier(SIGNER_KEY.getBytes())) || expiryTime.before(new Date())) {
            log.error("Token verification failed or token expired");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            log.warn("Token is already invalidated: {}", signedJWT.getJWTClaimsSet().getJWTID());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    // Xây dựng scope dựa trên các role của user
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add("ROLE_" + role.getName()));
        }
        return stringJoiner.toString();
    }
}
