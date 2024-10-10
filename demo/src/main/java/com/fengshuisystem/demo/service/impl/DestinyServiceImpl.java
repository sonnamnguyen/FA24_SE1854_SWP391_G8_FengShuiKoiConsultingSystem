package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
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
        return destinyRepository.findAllByAnimalId(animalId)
                .stream()
                .map(destinyMapper::toDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getShapeNames(Integer destinyId) {
        return shapeServiceImpl.getShapesByDestiny(destinyId).stream()
                .map(ShapeDTO::getShape)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getColorNames(Integer destinyId) {
        return colorServiceImpl.getColorsByDestiny(destinyId).stream()
                .map(ColorDTO::getColor)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getDirectionNames(Integer destinyId) {
        return directionServiceImpl.getDirections(destinyId).stream()
                .map(DirectionDTO::getDirection)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<Integer> getNumberNames(Integer destinyId) {
        return numberServiceImpl.getNumbers(destinyId).stream()
                .map(NumberDTO::getNumber)
                .collect(Collectors.toList());
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getAnimalNames(Integer destinyId, String tuongKhacTruoc, String tuongKhacSau) {
        List<ColorDTO> colors = colorServiceImpl.getColorsByDestiny(destinyId);
        return colors.stream()
                .flatMap(color -> animalService.getAnimalCategoryByColorId(color.getId()).stream())
                .filter(animalCategory -> {
                    List<String> animalDestinies = getAllDestinyByAnimal(animalCategory.getId()).stream()
                            .map(DestinyDTO::getDestiny)
                            .toList();
                    return animalDestinies.stream()
                            .noneMatch(destinyItem -> destinyItem.equals(tuongKhacTruoc) || destinyItem.equals(tuongKhacSau));
                })
                .map(AnimalCategoryDTO::getAnimalCategoryName)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getShelterNames(Integer destinyId) {
        List<ShapeDTO> shapes = shapeServiceImpl.getShapesByDestiny(destinyId);
        return shapes.stream()
                .flatMap(shape -> shelterService.getAllSheltersByShape(shape.getId()).stream())
                .map(ShelterCategoryDTO::getShelterCategoryName)
                .collect(Collectors.toList());
    }

}
