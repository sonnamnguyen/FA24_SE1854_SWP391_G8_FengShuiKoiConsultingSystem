package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.DestinyYearDTO;
import com.fengshuisystem.demo.dto.PageResponse;

import java.util.List;

public interface DestinyYearService {
    List<Integer> getAllYear();
    DestinyYearDTO createDestinyYear(DestinyYearDTO request);
    PageResponse<DestinyYearDTO> getDestinyYears(int page, int size);
    DestinyYearDTO updateDestinyYear(Integer id, DestinyYearDTO request);
    void deleteDestinyYear(Integer id);
}
