package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ShelterImageDTO;

import java.util.List;

public interface ShelterImageService {
    List<ShelterImageDTO> getAllShelterImage(Integer shelterId, List<String> imgUrl);
}