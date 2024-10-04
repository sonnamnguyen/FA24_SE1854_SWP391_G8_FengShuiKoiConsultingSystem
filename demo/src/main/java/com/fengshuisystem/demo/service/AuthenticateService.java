package com.fengshuisystem.demo.service;


import com.fengshuisystem.demo.dto.request.AuthenticationRequest;
import com.fengshuisystem.demo.dto.request.IntrospectRequest;
import com.fengshuisystem.demo.dto.request.LogoutRequest;
import com.fengshuisystem.demo.dto.request.RefreshRequest;
import com.fengshuisystem.demo.dto.response.AuthenticationResponse;
import com.fengshuisystem.demo.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;


import java.text.ParseException;

public interface AuthenticateService {
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    public AuthenticationResponse outboundAuthenticate(String code);
    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public void logout(LogoutRequest request) throws ParseException, JOSEException;
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

}
