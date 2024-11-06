package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ShelterImageDTO;
import com.fengshuisystem.demo.entity.ShelterImage;
import com.fengshuisystem.demo.mapper.ShelterImageMapper;
import com.fengshuisystem.demo.repository.ShelterImageRepository;
import com.fengshuisystem.demo.service.ShelterImageService;
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
public class ShelterImageServiceImpl implements ShelterImageService {

    ShelterImageRepository shelterImageRepository;
    ShelterImageMapper shelterImageMapper;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<ShelterImageDTO> getAllShelterImage(Integer id, List<String> imgUrl) {
        List<ShelterImage> shelterImages = shelterImageRepository.findByShelterCategoryAndImgUrls(id, imgUrl);
        if(!shelterImages.isEmpty()){
            shelterImageRepository.deleteAll(shelterImages);
        }
        return shelterImageMapper.toDto(shelterImages);
    }
}