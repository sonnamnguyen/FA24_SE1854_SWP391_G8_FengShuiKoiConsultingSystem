package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.Destiny2DTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.request.Destiny2CompatibilityRequest;
import com.fengshuisystem.demo.dto.response.Destiny2CompatibilityResponse;
import com.fengshuisystem.demo.dto.response.Destiny2Response;

public interface Destiny2Service {
    Destiny2DTO createDestiny(Destiny2DTO request);
    PageResponse<Destiny2DTO> getDestinies(int page, int size);
    Destiny2DTO getDestinyById(Integer id);
    Destiny2DTO updateDestiny(Integer id, Destiny2DTO request);
    void deleteDestiny(Integer id);
    Destiny2Response getDestinyInfoByYear(int year);
    Destiny2CompatibilityResponse calculateCompatibility(int year, Destiny2CompatibilityRequest request);
}
