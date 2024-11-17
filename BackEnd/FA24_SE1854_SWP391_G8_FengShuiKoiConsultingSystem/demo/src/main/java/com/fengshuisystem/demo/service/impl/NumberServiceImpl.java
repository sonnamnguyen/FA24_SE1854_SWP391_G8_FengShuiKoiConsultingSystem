package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.NumberDTO;
import com.fengshuisystem.demo.entity.Number;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.NumberMapper;
import com.fengshuisystem.demo.repository.NumberRepository;
import com.fengshuisystem.demo.service.NumberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NumberServiceImpl implements NumberService {

    NumberRepository numberRepository;
    NumberMapper numberMapper;

    @Override
    public List<NumberDTO> getNumbers(Integer destiny) {
        List<NumberDTO> numbers = numberRepository.findAllByDestiny(destiny)
                .stream()
                .map(numberMapper::toDto)
                .toList();
        if (numbers.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return numbers;
    }

    @Override
    public List<NumberDTO> getAllNumbers() {
        List<NumberDTO> numbers = numberRepository.findAll()
                .stream()
                .map(numberMapper::toDto)
                .toList();
        if (numbers.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return numbers;
    }
}



