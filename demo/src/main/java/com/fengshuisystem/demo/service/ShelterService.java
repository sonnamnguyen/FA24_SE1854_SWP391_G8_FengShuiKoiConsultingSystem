package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;


public interface ShelterService {
     ShelterCategoryDTO createShelter(ShelterCategoryDTO request);
     PageResponse<ShelterCategoryDTO> getSheltersBySearch(String name, int page, int size);
     PageResponse<ShelterCategoryDTO> getAllShelters(int page, int size);
     void deleteShelter(Integer id);
     ShelterCategoryDTO updateShelter(Integer id, ShelterCategoryDTO request);
}
