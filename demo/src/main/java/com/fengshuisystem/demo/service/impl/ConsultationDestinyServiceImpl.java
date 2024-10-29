package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationDestinyDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.request.ConsultationDestinyCompatibilityRequest;
import com.fengshuisystem.demo.dto.response.ColorResponse;
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
import java.util.ArrayList;
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
        DestinyYear destinyYearOpt = destinyYearRepository.findByYear(year, Status.ACTIVE);
        String destiny = destinyYearOpt.getDestiny();
        List<String> destinyHuong = consultationDestinyRepository.findByCodesAndStatus(destiny, "HUONG", Status.ACTIVE)
                .stream().map(ConsultationDestiny::getName).toList();
        List<String> destinySo = consultationDestinyRepository.findByCodesAndStatus(destiny, "SO", Status.ACTIVE)
                .stream().map(ConsultationDestiny::getName).toList();
        List<String> destinyMau = consultationDestinyRepository.findByCodesAndStatus(destiny, "MAU", Status.ACTIVE)
                .stream().map(ConsultationDestiny::getName).toList();
        return ConsultationDestinyResponse.builder()
                .destiny(destiny)
                .direction(destinyHuong)
                .quantity(destinySo)
                .color(destinyMau)
                .build();
    }

    @Override
    public ConsultationDestinyCompatibilityResponse calculateCompatibility(int year, ConsultationDestinyCompatibilityRequest request) {
        DestinyYear destinyYearOpt = destinyYearRepository.findByYear(year, Status.ACTIVE);
        String destiny = destinyYearOpt.getDestiny();

        ConsultationDestinyDTO destinyHuong = null;
        ConsultationDestinyDTO destinySo = null;
        List<ColorResponse> colorResponses = new ArrayList<>();
        double totalColorScore = 0;
        double totalScore = 0;
        if (request.getDirectionName() != null) {
            destinyHuong = consultationDestinyMapper.toDto(
                    consultationDestinyRepository.findByCodesAndStatusAndName(destiny, "HUONG", Status.ACTIVE, request.getDirectionName())
            );
        }
        if (request.getQuantityName() != null) {
            destinySo = consultationDestinyMapper.toDto(
                    consultationDestinyRepository.findByCodesAndStatusAndName(destiny, "SO", Status.ACTIVE, request.getQuantityName().toString())
            );
        }
        if (request.getColorName() != null) {
            for (String colorName : request.getColorName()) {
                ConsultationDestinyDTO destinyMau = consultationDestinyMapper.toDto(
                        consultationDestinyRepository.findByCodesAndStatusAndName(destiny, "MAU", Status.ACTIVE, colorName)
                );
                if (destinyMau != null) {
                    Integer colorScore = destinyMau.getMinValue();
                    totalColorScore += colorScore;
                    ColorResponse colorResponse = new ColorResponse();
                    colorResponse.setColor(colorName);
                    colorResponse.setColorScore(colorScore);
                    colorResponses.add(colorResponse);
                }
                else{
                    Integer colorScore = 0;
                    ColorResponse colorResponse = new ColorResponse();
                    colorResponse.setColor(colorName);
                    colorResponse.setColorScore(colorScore);
                    colorResponses.add(colorResponse);
                }
            }
        }

        double colorAverageScore = colorResponses.isEmpty() ? 0.0 : Math.round(totalColorScore / colorResponses.size() * 100.0) / 100.0;

        totalScore += colorAverageScore;

        ConsultationDestinyCompatibilityResponse.ConsultationDestinyCompatibilityResponseBuilder responseBuilder =
                ConsultationDestinyCompatibilityResponse.builder()
                        .direction(request.getDirectionName())
                        .quantity(request.getQuantityName().toString())
                        .color(colorResponses)
                        .colorAverageScore(colorAverageScore);

        if (destinyHuong != null) {
            responseBuilder.directionScore(destinyHuong.getMinValue());
            totalScore += destinyHuong.getMinValue();

        } else {
            responseBuilder.directionScore(0);
        }

        if (destinySo != null) {
            responseBuilder.quantityScore(destinySo.getMinValue());
            totalScore += destinySo.getMinValue();
        } else {
            responseBuilder.quantityScore(0);
        }

        return responseBuilder.totalScore(totalScore).build();
    }
}

