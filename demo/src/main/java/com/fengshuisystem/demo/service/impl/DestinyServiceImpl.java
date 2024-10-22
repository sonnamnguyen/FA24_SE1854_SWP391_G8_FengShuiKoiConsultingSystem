package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.DestinyDTO;
import com.fengshuisystem.demo.mapper.DestinyMapper;
import com.fengshuisystem.demo.repository.DestinyRepository;
import com.fengshuisystem.demo.service.DestinyService;
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
public class DestinyServiceImpl implements DestinyService {
    DestinyRepository destinyRepository;
    DestinyMapper destinyMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<DestinyDTO> getAllDestiny() {
        return destinyMapper.toDto(destinyRepository.findAll());
    }
}
