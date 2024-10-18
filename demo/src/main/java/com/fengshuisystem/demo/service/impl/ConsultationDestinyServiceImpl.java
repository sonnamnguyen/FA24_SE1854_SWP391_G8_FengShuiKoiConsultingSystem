package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationDestinyDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.request.ConsultationDestinyCompatibilityRequest;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyCompatibilityResponse;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyResponse;
import com.fengshuisystem.demo.entity.ConsultationDestiny;
import com.fengshuisystem.demo.entity.DestinyYear;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.ConsultationDestinyMapper;
import com.fengshuisystem.demo.repository.ConsultationDestinyRepository;
import com.fengshuisystem.demo.repository.DestinyYearRepository;
import com.fengshuisystem.demo.service.ConsultationDestinyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsultationDestinyServiceImpl implements ConsultationDestinyService {

    ConsultationDestinyRepository consultationDestinyRepository;
    ConsultationDestinyMapper consultationDestinyMapper;
    DestinyYearRepository destinyYearRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationDestinyDTO createDestiny(ConsultationDestinyDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        ConsultationDestiny destiny = consultationDestinyMapper.toEntity(request);
        destiny.setCreatedBy(name);
        destiny.setStatus(Status.ACTIVE);
        destiny.setCreatedDate(Instant.now());
        return consultationDestinyMapper.toDto(consultationDestinyRepository.save(destiny));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ConsultationDestinyDTO> getDestinies(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = consultationDestinyRepository.findAllByStatus(Status.ACTIVE, pageable);

        if (pageData.isEmpty()) {
            throw new AppException(ErrorCode.DESTINY_NOT_EXISTED);
        }

        return PageResponse.<ConsultationDestinyDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(consultationDestinyMapper::toDto).toList())
                .build();
    }



    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ConsultationDestinyDTO updateDestiny(Integer id, ConsultationDestinyDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        ConsultationDestiny destiny = consultationDestinyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        consultationDestinyMapper.update(request, destiny);
        destiny.setUpdatedBy(name);
        destiny.setUpdatedDate(Instant.now());
        return consultationDestinyMapper.toDto(consultationDestinyRepository.save(destiny));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDestiny(Integer id) {
        var destiny = consultationDestinyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        destiny.setStatus(Status.DELETED);
        consultationDestinyRepository.save(destiny);
    }

    @Override
    public ConsultationDestinyResponse getDestinyInfoByYear(int year) {
        DestinyYear destinyYearOpt = destinyYearRepository.findByYear(year,Status.ACTIVE);
        String destiny = destinyYearOpt.getDestiny();
        List<String> destinyHuong = consultationDestinyRepository.findByCodesAndStatus(destiny, "HUONG", Status.ACTIVE)
                .stream().map(ConsultationDestiny::getName).toList();
        List<String> destinySo = consultationDestinyRepository.findByCodesAndStatus(destiny, "SO", Status.ACTIVE)
                .stream().map(ConsultationDestiny::getName).toList();
        List<String> destinyMau = consultationDestinyRepository.findByCodesAndStatus(destiny, "MAU", Status.ACTIVE)
                .stream().map(ConsultationDestiny::getName).toList();
        return ConsultationDestinyResponse.builder()
                .Destiny(destiny)
                .huong(destinyHuong)
                .so(destinySo)
                .mau(destinyMau)
                .build();
    }

    @Override
    public ConsultationDestinyCompatibilityResponse calculateCompatibility(int year, ConsultationDestinyCompatibilityRequest request) {
        DestinyYear destinyYearOpt = destinyYearRepository.findByYear(year,Status.ACTIVE);
        String destiny = destinyYearOpt.getDestiny();

        ConsultationDestinyDTO destinyHuong = null;
        ConsultationDestinyDTO destinySo = null;
        ConsultationDestinyDTO destinyMau = null;

        if (request.getHuongName() != null) {
            destinyHuong = consultationDestinyMapper.toDto(consultationDestinyRepository.findByCodesAndStatusAndName(destiny,"HUONG", Status.ACTIVE, request.getHuongName()));
        }
        if (request.getSoName() != null) {
            destinySo = consultationDestinyMapper.toDto(consultationDestinyRepository.findByCodesAndStatusAndName(destiny,"SO", Status.ACTIVE, request.getSoName().toString()));
        }
        if (request.getMauName() != null) {
            destinyMau = consultationDestinyMapper.toDto(consultationDestinyRepository.findByCodesAndStatusAndName(destiny,"MAU", Status.ACTIVE, request.getMauName()));
        }
        ConsultationDestinyCompatibilityResponse.ConsultationDestinyCompatibilityResponseBuilder responseBuilder = ConsultationDestinyCompatibilityResponse.builder();
        responseBuilder.huong(request.getHuongName())
                .so(request.getSoName().toString())
                .mau(request.getMauName());
        if (destinyHuong != null ) {
            responseBuilder.huongScore(destinyHuong.getMinValue()+"/"+destinyHuong.getMaxValue());
        }else{
            responseBuilder.huongScore("0");
        }

        if (destinySo != null) {
            responseBuilder.soScore(destinySo.getMinValue()+"/"+destinySo.getMaxValue());
        }else{
            responseBuilder.soScore("0");
        }

        if (destinyMau != null) {
            responseBuilder.mauScore(destinyMau.getMinValue()+"/"+destinyMau.getMaxValue());
        }else{
            responseBuilder.mauScore("0");
        }
        return responseBuilder.build();
    }
}

