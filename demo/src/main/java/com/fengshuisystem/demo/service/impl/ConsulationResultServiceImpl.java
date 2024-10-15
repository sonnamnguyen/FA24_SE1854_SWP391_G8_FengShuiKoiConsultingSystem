package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.mapper.ConsulationResultMapper;
import com.fengshuisystem.demo.repository.ConsulationResultRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConsulationResultServiceImpl {
    ConsulationResultRepository consulationResultRepository;
    ConsulationResultMapper consulationResultMapper;

//    public PageResponse<ConsultationResultDTO> getConsulationBySearch(String name, int page, int size) {
//
//    }
}
