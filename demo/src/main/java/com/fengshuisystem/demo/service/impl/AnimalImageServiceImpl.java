package com.fengshuisystem.demo.service.impl;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.entity.AnimalImage;
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
    public List<AnimalImageDTO> getAllAnimalImage(Integer animalId, List<String> imgUrl) {
        List<AnimalImage> animalImages = animalImageRepository.findByAnimalCategoryAndImgUrls(animalId, imgUrl);
        if (!animalImages.isEmpty()) {
            animalImageRepository.deleteAll(animalImages);
        }
        return animalImageMapper.toDto(animalImages);
    }
}