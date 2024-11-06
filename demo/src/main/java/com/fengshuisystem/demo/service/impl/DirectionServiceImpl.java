package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.DirectionDTO;
import com.fengshuisystem.demo.dto.NumberDTO;
import com.fengshuisystem.demo.entity.Direction;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.DirectionMapper;
import com.fengshuisystem.demo.repository.DirectionRepository;
import com.fengshuisystem.demo.service.DirectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectionServiceImpl implements DirectionService {
    DirectionRepository directionRepository;
    DirectionMapper directionMapper;

    @Override
    public List<DirectionDTO> getDirections(Integer destiny) {
        List<DirectionDTO> directions = directionRepository.findAllByDestiny(destiny)
                .stream()
                .map(directionMapper::toDto)
                .toList();
        if (directions.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return directions;
    }
    @Override
    public List<DirectionDTO> getAllDirections() {
        List<DirectionDTO> directions = directionRepository.findAll()
                .stream()
                .map(directionMapper::toDto)
                .toList();
        if (directions.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return directions;
    }
}
