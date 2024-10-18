package com.fengshuisystem.demo.service;


import com.fengshuisystem.demo.dto.request.DestinyRequest;
import com.fengshuisystem.demo.dto.response.CompatibilityResultResponse;

public interface CompatibilityResultResponseService {
     String compareDestinyWithExplanation(String userDestiny, String attributeDestiny, String attributeName);
     CompatibilityResultResponse calculateCompatibility( int yearOfBirth, DestinyRequest destinyInput);
}
