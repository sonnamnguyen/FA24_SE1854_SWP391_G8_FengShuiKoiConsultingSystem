package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponseContainer;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.DestinyMapper;
import com.fengshuisystem.demo.mapper.ShapeMapper;
import com.fengshuisystem.demo.repository.DestinyRepository;
import com.fengshuisystem.demo.service.DestinyService;
import com.fengshuisystem.demo.service.ShelterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DestinyServiceImpl implements DestinyService {

    DestinyRepository destinyRepository;
    DestinyMapper destinyMapper;
    NumberServiceImpl numberServiceImpl;
    DirectionServiceImpl directionServiceImpl;
    ShapeServiceImpl shapeServiceImpl;
    ColorServiceImpl colorServiceImpl;
    AnimalServiceImpl animalService;
    ShelterService shelterService;

    // Bảng Can (0-9 tương ứng với Canh, Tân, Nhâm, Quý, Giáp, Ất, Bính, Đinh, Mậu, Kỷ)
    int[] HEAVENLY_STEM_VALUES = {4, 4, 5, 5, 1, 1, 2, 2, 3, 3};
    // Bảng Chi (0-11 tương ứng với Thân, Dậu, Tuất, Hợi, Tý, Sửu, Dần, Mão, Thìn, Tỵ, Ngọ, Mùi)
    int[] EARTHLY_BRANCH_VALUES = {1, 1, 2, 2, 0, 0, 1, 1, 2, 2, 0, 0};

    List<String> tuongSinhList = Arrays.asList("KIM", "THỦY", "MỘC", "HỎA", "THỔ");
    List<String> tuongKhacList = Arrays.asList("KIM", "MỘC", "THỦY", "HỎA", "THỔ");

    String[] ELEMENTS = {"KIM", "THỦY", "HỎA", "THỔ", "MỘC"};
    private final ShapeMapper shapeMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    public String getDestinyFromYear(int yearOfBirth) {
        int heavenlyStem = HEAVENLY_STEM_VALUES[yearOfBirth % 10];
        int earthlyBranch = EARTHLY_BRANCH_VALUES[yearOfBirth % 12];
        int elementIndex = heavenlyStem + earthlyBranch;
        if (elementIndex > 5) {
            elementIndex -= 5;
        }
        return ELEMENTS[elementIndex - 1];
    }

    @Override
    public String findTuongSinhTruoc(String destiny) {
        int index = tuongSinhList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongSinhList.get((index - 1 + tuongSinhList.size()) % tuongSinhList.size());
    }

    @Override
    public String findTuongSinhSau(String destiny) {
        int index = tuongSinhList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongSinhList.get((index + 1) % tuongSinhList.size());
    }

    @Override
    public String findTuongKhacTruoc(String destiny) {
        int index = tuongKhacList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongKhacList.get((index - 1 + tuongKhacList.size()) % tuongKhacList.size());
    }

    @Override
    public String findTuongKhacSau(String destiny) {
        int index = tuongKhacList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongKhacList.get((index + 1) % tuongKhacList.size());
    }
    @Override
    @PreAuthorize("hasRole('USER')")
    public DestinyDTO getDestinyId(String destinyName) {
        return destinyMapper.toDto(destinyRepository.findByDestiny(destinyName));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<DestinyDTO> getAllDestinyByAnimal(int animalId) {
        List<DestinyDTO> destinies = destinyRepository.findAllByAnimalId(animalId)
                .stream()
                .map(destinyMapper::toDto)
                .toList();
        if (destinies.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return destinies;
    }

    @PreAuthorize("hasRole('USER')")
    public AutoConsultationResponseDTO autoConsultationVipPro(int year) {
        String destiny = getDestinyFromYear(year);

        String tuongKhacTruoc = findTuongKhacTruoc(destiny);
        String tuongKhacSau = findTuongKhacSau(destiny);

        DestinyDTO tuongHopId = getDestinyId(destiny);

        return AutoConsultationResponseDTO.builder()
                .message("Mang lại sự ổn định, hòa thuận và bền vững")
                .destiny(destiny)
                .numbers(getNumberNames(tuongHopId.getId()))
                .directions(getDirectionNames(tuongHopId.getId()))
                .shapes(getShapeNames(tuongHopId.getId()))
                .colors(getColorNames(tuongHopId.getId()))
                .shelters(getShelterNames(tuongHopId.getId()))
                .animals(getAnimalNames(tuongHopId.getId(), tuongKhacTruoc, tuongKhacSau))
                .build();
    }
    @PreAuthorize("hasRole('USER')")
    public AutoConsultationResponseDTO autoConsultationVipPro2(int year) {
        String destiny = getDestinyFromYear(year);

        String tuongSinhruoc = findTuongSinhTruoc(destiny);
        String tuongKhacTruoc = findTuongKhacTruoc(destiny);
        String tuongKhacSau = findTuongKhacSau(destiny);

        DestinyDTO tuongSinhId = getDestinyId(tuongSinhruoc);

        return AutoConsultationResponseDTO.builder()
                .message("Mang đến sự phát triển, hỗ trợ và thăng tiến")
                .destiny(destiny)
                .numbers(getNumberNames(tuongSinhId.getId()))
                .directions(getDirectionNames(tuongSinhId.getId()))
                .shapes(getShapeNames(tuongSinhId.getId()))
                .colors(getColorNames(tuongSinhId.getId()))
                .shelters(getShelterNames(tuongSinhId.getId()))
                .animals(getAnimalNames(tuongSinhId.getId(), tuongKhacTruoc, tuongKhacSau))
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    public AutoConsultationResponseContainer autoConsultationResponseContainer(int year){
        return AutoConsultationResponseContainer.builder()
                .consultation1(autoConsultationVipPro2(year))
                .consultation2(autoConsultationVipPro(year))
                .build();
    }


    @PreAuthorize("hasRole('USER')")
    public List<String> getShapeNames(Integer destinyId) {
        return shapeServiceImpl.getShapesByDestiny(destinyId).stream()
                .map(ShapeDTO::getShape)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<String> getColorNames(Integer destinyId) {
        return colorServiceImpl.getColorsByDestiny(destinyId).stream()
                .map(ColorDTO::getColor)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<String> getDirectionNames(Integer destinyId) {
        return directionServiceImpl.getDirections(destinyId).stream()
                .map(DirectionDTO::getDirection)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<Integer> getNumberNames(Integer destinyId) {
        return numberServiceImpl.getNumbers(destinyId).stream()
                .map(NumberDTO::getNumber)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('USER')")
    public List<String> getAnimalNames(Integer destinyId, String tuongKhacTruoc, String tuongKhacSau) {
        List<ColorDTO> colors = colorServiceImpl.getColorsByDestiny(destinyId);
        return colors.stream()
                .flatMap(color -> animalService.getAnimalCategoryByColorId(color.getId()).stream())
                .filter(animalCategory -> {
                    List<String> animalDestinies = destinyRepository.findAllByAnimalId(animalCategory.getId())
                            .stream()
                            .map(destinyMapper::toDto)
                            .map(DestinyDTO::getDestiny)
                            .toList();
                    return animalDestinies.stream()
                            .noneMatch(destinyItem -> destinyItem.equals(tuongKhacTruoc) || destinyItem.equals(tuongKhacSau));
                })
                .map(AnimalCategoryDTO::getAnimalCategoryName)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<String> getShelterNames(Integer destinyId) {
        List<ShapeDTO> shapes = shapeServiceImpl.getShapesByDestiny(destinyId);
        return shapes.stream()
                .flatMap(shape -> shelterService.getAllSheltersByShape(shape.getId()).stream())
                .map(ShelterCategoryDTO::getShelterCategoryName)
                .collect(Collectors.toList());
    }

}
