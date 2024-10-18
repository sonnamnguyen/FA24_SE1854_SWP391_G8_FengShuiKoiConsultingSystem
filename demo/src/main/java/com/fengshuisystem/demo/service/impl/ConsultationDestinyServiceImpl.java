package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationDestinyDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.request.ConsultationDestinyCompatibilityRequest;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyCompatibilityResponse;
import com.fengshuisystem.demo.dto.response.ConsultationDestinyResponse;
import com.fengshuisystem.demo.entity.ConsultationDestiny;
import com.fengshuisystem.demo.entity.DestinyYear;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.mapper.ConsultationDestinyMapper;
import com.fengshuisystem.demo.repository.ConsultationDestinyRepository;
import com.fengshuisystem.demo.repository.DestinyYearRepository;
import com.fengshuisystem.demo.service.ConsultationDestinyService;
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
public class ConsultationDestinyServiceImpl implements ConsultationDestinyService {

    ConsultationDestinyRepository consultationDestinyRepository;
    ConsultationDestinyMapper consultationDestinyMapper;
    DestinyYearRepository destinyYearRepository;

    @Override
    public ConsultationDestinyDTO createDestiny(ConsultationDestinyDTO request) {
        return null;
    }

    @Override
    public PageResponse<ConsultationDestinyDTO> getDestinies(int page, int size) {
        return null;
    }

    @Override
    public ConsultationDestinyDTO getDestinyById(Integer id) {
        return null;
    }

    @Override
    public ConsultationDestinyDTO updateDestiny(Integer id, ConsultationDestinyDTO request) {
        return null;
    }

    @Override
    public void deleteDestiny(Integer id) {

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

