package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationShelterDTO;
import com.fengshuisystem.demo.entity.ConsultationShelter;
import com.fengshuisystem.demo.mapper.ConsultationShelterMapper;
import com.fengshuisystem.demo.repository.ConsultationShelterRepository;
import com.fengshuisystem.demo.service.ConsultationShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationShelterServiceImpl implements ConsultationShelterService {

    @Override
    public ConsultationShelterDTO createConsultationShelter(ConsultationShelterDTO dto, Integer requestDetailId, Integer shelterCategoryId) {
        return null;
    }

    @Override
    public List<ConsultationShelterDTO> getSheltersByRequestDetail(Integer requestDetailId) {
        return List.of();
    }
}
