package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.mapper.AnimalImageMapper;
import com.fengshuisystem.demo.repository.AnimalImageRepository;
import com.fengshuisystem.demo.service.AnimalImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AnimalImageServiceImpl implements AnimalImageService {
    AnimalImageRepository animalImageRepository;
    AnimalImageMapper animalImageMapper;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AnimalImageDTO> getAllAnimalImage(Integer animalId) {
        return animalImageMapper.toDto(animalImageRepository.findByAnimalCategory(animalId));
    }
}
