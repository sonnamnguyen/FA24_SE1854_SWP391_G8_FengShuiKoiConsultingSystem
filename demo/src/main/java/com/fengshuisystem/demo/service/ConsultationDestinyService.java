package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationDestinyDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.request.ConsultationDestinyCompatibilityRequest;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyCompatibilityResponse;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyResponse;

public interface ConsultationDestinyService {
    ConsultationDestinyDTO createDestiny(ConsultationDestinyDTO request);
    PageResponse<ConsultationDestinyDTO> getDestinies(int page, int size);
    ConsultationDestinyDTO updateDestiny(Integer id, ConsultationDestinyDTO request);
    void deleteDestiny(Integer id);
    ConsultationDestinyResponse getDestinyInfoByYear(int year);
    ConsultationDestinyCompatibilityResponse calculateCompatibility(int year, ConsultationDestinyCompatibilityRequest request);
}
