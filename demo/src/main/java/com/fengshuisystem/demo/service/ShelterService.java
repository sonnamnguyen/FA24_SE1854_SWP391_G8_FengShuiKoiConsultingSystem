
package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;

import java.util.List;


public interface ShelterService {
    ShelterCategoryDTO createShelter(ShelterCategoryDTO request);
    PageResponse<ShelterCategoryDTO> getSheltersBySearch(String name, int page, int size);
    PageResponse<ShelterCategoryDTO> getAllShelters(int page, int size);
    void deleteShelter(Integer id);
    ShelterCategoryDTO updateShelter(Integer id, ShelterCategoryDTO request);
    List<ShelterCategoryDTO> getAllSheltersByShape(Integer shape);
    ShelterCategoryDTO getShelterById(Integer id);
    List<ShelterCategoryDTO> getAllShelterCategory();
    PageResponse<ShelterCategoryDTO> getSheltersByDestiny(List<String> destiny, int page, int size);
}
