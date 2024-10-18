package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.Destiny2DTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.request.Destiny2CompatibilityRequest;
import com.fengshuisystem.demo.dto.response.Destiny2CompatibilityResponse;
import com.fengshuisystem.demo.dto.response.Destiny2Response;
import com.fengshuisystem.demo.entity.ConsultationDestiny;
import com.fengshuisystem.demo.entity.DestinyYear;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.Destiny2Mapper;
import com.fengshuisystem.demo.repository.Destiny2Repository;
import com.fengshuisystem.demo.repository.DestinyYearRepository;
import com.fengshuisystem.demo.service.Destiny2Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Destiny2ServiceImpl implements Destiny2Service {
    Destiny2Repository destiny2Repository;
    Destiny2Mapper destiny2Mapper;
    DestinyYearRepository destinyYearRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Destiny2DTO createDestiny(Destiny2DTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        ConsultationDestiny destiny = destiny2Mapper.toEntity(request);
        destiny.setCreatedBy(name);
        destiny.setCreatedDate(Instant.now());
        return destiny2Mapper.toDto(destiny2Repository.save(destiny));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<Destiny2DTO> getDestinies(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = destiny2Repository.findAll(pageable);

        if (pageData.isEmpty()) {
            throw new AppException(ErrorCode.DESTINY_NOT_EXISTED);
        }

        return PageResponse.<Destiny2DTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(destiny2Mapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Destiny2DTO getDestinyById(Integer id) {
        ConsultationDestiny destiny = destiny2Repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        return destiny2Mapper.toDto(destiny);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Destiny2DTO updateDestiny(Integer id, Destiny2DTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        ConsultationDestiny destiny = destiny2Repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        destiny2Mapper.update(request, destiny);
        destiny.setUpdatedBy(name);
        destiny.setUpdatedDate(Instant.now());
        return destiny2Mapper.toDto(destiny2Repository.save(destiny));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDestiny(Integer id) {
        var destiny = destiny2Repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        destiny.setStatus(Status.DELETED);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Destiny2Response getDestinyInfoByYear(int year) {
        DestinyYear destinyYearOpt = destinyYearRepository.findByYear(year);
            String destiny = destinyYearOpt.getDestiny();
            List<String> destinyHuong = destiny2Repository.findByCodesAndStatus(destiny, "HUONG", Status.ACTIVE)
                    .stream().map(ConsultationDestiny::getName).toList();
            List<String> destinySo = destiny2Repository.findByCodesAndStatus(destiny, "SO", Status.ACTIVE)
                    .stream().map(ConsultationDestiny::getName).toList();
            List<String> destinyMau = destiny2Repository.findByCodesAndStatus(destiny, "MAU", Status.ACTIVE)
                    .stream().map(ConsultationDestiny::getName).toList();
            return Destiny2Response.builder()
                    .Destiny(destiny)
                    .huong(destinyHuong)
                    .so(destinySo)
                    .mau(destinyMau)
                    .build();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Destiny2CompatibilityResponse calculateCompatibility(int year, Destiny2CompatibilityRequest request) {
        DestinyYear destinyYearOpt = destinyYearRepository.findByYear(year);
            String destiny = destinyYearOpt.getDestiny();

            Destiny2DTO destinyHuong = null;
            Destiny2DTO destinySo = null;
            Destiny2DTO destinyMau = null;

            if (request.getHuongName() != null) {
                destinyHuong = destiny2Mapper.toDto(destiny2Repository.findByCodesAndStatusAndName(destiny,"HUONG", Status.ACTIVE, request.getHuongName()));
            }
            if (request.getSoName() != null) {
                destinySo = destiny2Mapper.toDto(destiny2Repository.findByCodesAndStatusAndName(destiny,"SO", Status.ACTIVE, request.getSoName().toString()));
            }
            if (request.getMauName() != null) {
                destinyMau = destiny2Mapper.toDto(destiny2Repository.findByCodesAndStatusAndName(destiny,"MAU", Status.ACTIVE, request.getMauName()));
            }
            Destiny2CompatibilityResponse.Destiny2CompatibilityResponseBuilder responseBuilder = Destiny2CompatibilityResponse.builder();
            responseBuilder.huong(request.getHuongName())
                    .so(request.getSoName().toString())
                    .mau(request.getMauName());
            if (destinyHuong != null ) {
                responseBuilder.huongScore(destinyHuong.getMinValue());
            }else{
                responseBuilder.huongScore(0);
            }

            if (destinySo != null) {
                responseBuilder.soScore(destinySo.getMinValue());
            }else{
                responseBuilder.soScore(0);
            }

            if (destinyMau != null) {
                responseBuilder.mauScore(destinyMau.getMinValue());
            }else{
                responseBuilder.mauScore(0);
            }
            return responseBuilder.build();
    }
}


