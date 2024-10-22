package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationAnimalDTO;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.Number;
import com.fengshuisystem.demo.entity.enums.ConsulationAnimalStatus;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationAnimalMapper;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.ConsulationAnimalService;
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
public class ConsulationAnimalServiceImpl implements ConsulationAnimalService {
    ConsultationAnimalMapper consultationAnimalMapper;
    ConsulationRequestDetailsRepository consulationRequestDetailsRepository;
    ConsulationResultRepository consulationResultRepository;
    NumberRepository numberRepository;
    ConsulationAnimalRepository consulationAnimalRepository;
    AnimalRepository animalRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationAnimalDTO createConsulationAnimal(Integer id, List<Integer> ids, ConsultationAnimalDTO consultationAnimalDTO) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Set<Number> numbers = numberRepository.findAllByIdIn(ids);
        ConsultationResult consultationResult = consulationResultRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONSULATION_RESULT_NOT_EXISTED));
        ConsultationRequestDetail consultationRequestDetails = consulationRequestDetailsRepository.findByRequestDetailId(consultationResult.getRequestDetail().getId());
        AnimalCategory animalCategory = animalRepository.findById(consultationRequestDetails.getAnimalCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        ConsultationAnimal consultationAnimal = consultationAnimalMapper.toEntity(consultationAnimalDTO);
            consultationAnimal.setAnimalCategory(animalCategory);
            consultationAnimal.setCreatedBy(name);
            consultationAnimal.setCreatedDate(Instant.now());
            consultationAnimal.setStatus(Status.COMPLETED);
            consultationAnimal.setUpdatetedDate(Instant.now());
            consultationAnimal.setUpdatetedBy(name);
            consultationAnimal.setNumbers(numbers);
            consultationAnimal.setConsultation(consultationResult);
           return consultationAnimalMapper.toDto(consulationAnimalRepository.save(consultationAnimal));
    }
}
