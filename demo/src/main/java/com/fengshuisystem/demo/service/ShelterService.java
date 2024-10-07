package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;


public interface ShelterService {
    public ShelterCategoryDTO createShelter(ShelterCategoryDTO request);
    public PageResponse<ShelterCategoryDTO> getSheltersBySearch(String name, int page, int size);
    public PageResponse<ShelterCategoryDTO> getAllShelters(int page, int size);
    public void deleteShelter(Integer id);
    public ShelterCategoryDTO updateShelter(Integer id, ShelterCategoryDTO request);
}
