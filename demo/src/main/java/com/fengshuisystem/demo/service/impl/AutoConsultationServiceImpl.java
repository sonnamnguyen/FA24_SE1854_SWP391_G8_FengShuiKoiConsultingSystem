package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.AutoConsultationResponseDTO;
import com.fengshuisystem.demo.dto.DestinyDTO;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponseContainer;
import com.fengshuisystem.demo.service.AutoConsultationService;
import com.fengshuisystem.demo.service.DestinyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoConsultationServiceImpl implements AutoConsultationService {

    DestinyService destinyService;


    @Override
    @PreAuthorize("hasRole('USER')")
    public AutoConsultationResponseDTO autoConsultationVipPro(int year) {
        String destiny = destinyService.getDestinyFromYear(year);

        String tuongKhacTruoc = destinyService.findTuongKhacTruoc(destiny);
        String tuongKhacSau = destinyService.findTuongKhacSau(destiny);

        DestinyDTO tuongHopId = destinyService.getDestinyId(destiny);

        return AutoConsultationResponseDTO.builder()
                .message("Mang lại sự ổn định, hòa thuận và bền vững")
                .destiny(destiny)
                .numbers(destinyService.getNumberNames(tuongHopId.getId()))
                .directions(destinyService.getDirectionNames(tuongHopId.getId()))
                .shapes(destinyService.getShapeNames(tuongHopId.getId()))
                .colors(destinyService.getColorNames(tuongHopId.getId()))
                .shelters(destinyService.getShelterNames(tuongHopId.getId()))
                .animals(destinyService.getAnimalNames(tuongHopId.getId(), tuongKhacTruoc, tuongKhacSau))
                .build();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public AutoConsultationResponseDTO autoConsultationVipPro2(int year) {
        String destiny = destinyService.getDestinyFromYear(year);

        String tuongSinhruoc = destinyService.findTuongSinhTruoc(destiny);
        String tuongKhacTruoc = destinyService.findTuongKhacTruoc(destiny);
        String tuongKhacSau = destinyService.findTuongKhacSau(destiny);

        DestinyDTO tuongSinhId = destinyService.getDestinyId(tuongSinhruoc);

        return AutoConsultationResponseDTO.builder()
                .message("Mang đến sự phát triển, hỗ trợ và thăng tiến")
                .destiny(destiny)
                .numbers(destinyService.getNumberNames(tuongSinhId.getId()))
                .directions(destinyService.getDirectionNames(tuongSinhId.getId()))
                .shapes(destinyService.getShapeNames(tuongSinhId.getId()))
                .colors(destinyService.getColorNames(tuongSinhId.getId()))
                .shelters(destinyService.getShelterNames(tuongSinhId.getId()))
                .animals(destinyService.getAnimalNames(tuongSinhId.getId(), tuongKhacTruoc, tuongKhacSau))
                .build();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public AutoConsultationResponseContainer autoConsultationResponseContainer(int year){
        return AutoConsultationResponseContainer.builder()
                .consultation1(autoConsultationVipPro2(year))
                .consultation2(autoConsultationVipPro(year))
                .build();
    }
}
