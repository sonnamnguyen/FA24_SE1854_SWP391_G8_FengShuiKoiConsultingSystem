package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.AnimalImageDTO;

import java.util.List;

public interface AnimalImageService {
    public List<AnimalImageDTO> getAllAnimalImage(Integer animalId, List<String> imgUrl);
}