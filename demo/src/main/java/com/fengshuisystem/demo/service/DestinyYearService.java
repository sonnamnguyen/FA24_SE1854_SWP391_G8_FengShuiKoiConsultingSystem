package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.DestinyYearDTO;
import com.fengshuisystem.demo.dto.PageResponse;

public interface DestinyYearService {
    DestinyYearDTO createDestinyYear(DestinyYearDTO request);
    PageResponse<DestinyYearDTO> getDestinyYears(int page, int size);
    DestinyYearDTO updateDestinyYear(Integer id, DestinyYearDTO request);
    void deleteDestinyYear(Integer id);
}
