package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.Number;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationAnimalMapper;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.ConsultationAnimalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsultationAnimalServiceImpl implements ConsultationAnimalService {
    ConsultationAnimalMapper consultationAnimalMapper;
    ConsultationRequestDetailRepository consultationRequestDetailRepository;
    ConsultationResultRepository consultationResultRepository;
    NumberRepository numberRepository;
    ConsultationAnimalRepository consultationAnimalRepository;
    AnimalRepository animalRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationAnimalDTO createConsulationAnimal(Integer id, List<Integer> ids, ConsultationAnimalDTO consultationAnimalDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Set<Number> numbers = numberRepository.findAllByIdIn(ids);
        ConsultationResult consultationResult = consultationResultRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));
        ConsultationRequestDetail consultationRequestDetails = consultationRequestDetailRepository.findByRequestDetailId(consultationResult.getRequestDetail().getId());
        AnimalCategory animalCategory = animalRepository.findById(consultationRequestDetails.getAnimalCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        ConsultationAnimal consultationAnimal = consultationAnimalMapper.toEntity(consultationAnimalDTO);
            consultationAnimal.setAnimalCategory(animalCategory);
            consultationAnimal.setCreatedBy(name);
            consultationAnimal.setCreatedDate(Instant.now());
            consultationAnimal.setStatus(Request.COMPLETED);
            consultationAnimal.setUpdatedDate(Instant.now());
            consultationAnimal.setUpdatedBy(name);
            consultationAnimal.setNumbers(numbers);
            consultationAnimal.setConsultationResult(consultationResult);
           return consultationAnimalMapper.toDto(consultationAnimalRepository.save(consultationAnimal));
    }
}
