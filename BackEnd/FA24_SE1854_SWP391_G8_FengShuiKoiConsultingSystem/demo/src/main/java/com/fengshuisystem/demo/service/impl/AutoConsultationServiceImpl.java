package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponse;
import com.fengshuisystem.demo.dto.response.AutoConsultationContainerResponse;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoConsultationServiceImpl implements AutoConsultationService {

    DestinyService destinyService;
    DirectionService directionService;
    NumberService numberService;
    ColorService colorService;
    ShapeService shapeService;

    @Override
    public AutoConsultationResponse autoConsultationVipPro(int year) {
        String destiny = destinyService.getDestinyFromYear(year);
        return getAutoConsultationResponse(destiny, destinyService.getDestinyId(destiny));
    }
    @Override
    public AutoConsultationResponse autoConsultationVipPro2(int year) {
        String destiny = destinyService.getDestinyFromYear(year);
        String tuongSinhTruoc = destinyService.findTuongSinhTruoc(destiny);
        return getAutoConsultationResponse(destiny, destinyService.getDestinyId(tuongSinhTruoc));
    }

    private AutoConsultationResponse getAutoConsultationResponse(String destiny, DestinyDTO destinyId) {
        String tuongKhacTruoc = destinyService.findTuongKhacTruoc(destiny);
        String tuongKhacSau = destinyService.findTuongKhacSau(destiny);
        return AutoConsultationResponse.builder()
                .numbers(numberService.getNumbers(destinyId.getId()).stream().map(NumberDTO::getNumber).toList())
                .directions(directionService.getDirections(destinyId.getId()).stream().map(DirectionDTO::getDirection).toList())
                .shapes(shapeService.getShapesByDestiny(destinyId.getId()).stream().map(ShapeDTO::getShape).toList())
                .colors(colorService.getColorsByDestiny(destinyId.getId()).stream().map(ColorDTO::getColor).toList())
                .shelters(destinyService.getShelter(destinyId.getId()))
                .animals(destinyService.getAnimal(destinyId.getId(), tuongKhacTruoc, tuongKhacSau))
                .build();
    }

    @Override
    public AutoConsultationContainerResponse autoConsultationResponseContainer(int year){
        String destiny = destinyService.getDestinyFromYear(year);
        String tuongSinhTruoc = destinyService.findTuongSinhTruoc(destiny);
        return AutoConsultationContainerResponse.builder()
                .destiny(destiny)
                .destinyTuongSinh(tuongSinhTruoc)
                .consultation1(autoConsultationVipPro2(year))
                .consultation2(autoConsultationVipPro(year))
                .build();
    }
}