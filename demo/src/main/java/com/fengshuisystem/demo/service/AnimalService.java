package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.PageResponse;
public interface AnimalService {
    public AnimalCategoryDTO createAnimal(AnimalCategoryDTO request);
    public PageResponse<AnimalCategoryDTO> getAnimalsBySearch(AnimalCategoryDTO search, int page, int size);
    public PageResponse<AnimalCategoryDTO> getAnimals(int page, int size);
    public void deleteAnimal(Integer id);
    public AnimalCategoryDTO updateAnimal(Integer id, AnimalCategoryDTO request);
    }
