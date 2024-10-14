package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.mapper.DestinyMapper;
import com.fengshuisystem.demo.repository.DestinyRepository;
import com.fengshuisystem.demo.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    NumberService numberService;
    DirectionService directionService;
    ShapeService shapeService;
    ColorService colorService;
    AnimalService animalService;
    ShelterService shelterService;

    // Bảng Can (0-9 tương ứng với Canh, Tân, Nhâm, Quý, Giáp, Ất, Bính, Đinh, Mậu, Kỷ)
    int[] HEAVENLY_STEM_VALUES = {4, 4, 5, 5, 1, 1, 2, 2, 3, 3};
    // Bảng Chi (0-11 tương ứng với Thân, Dậu, Tuất, Hợi, Tý, Sửu, Dần, Mão, Thìn, Tỵ, Ngọ, Mùi)
    int[] EARTHLY_BRANCH_VALUES = {1, 1, 2, 2, 0, 0, 1, 1, 2, 2, 0, 0};

    List<String> tuongSinhList = Arrays.asList("KIM", "THỦY", "MỘC", "HỎA", "THỔ");
    List<String> tuongKhacList = Arrays.asList("KIM", "MỘC", "THỔ", "THỦY", "HỎA");

    String[] ELEMENTS = {"KIM", "THỦY", "HỎA", "THỔ", "MỘC"};

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
    public DestinyDTO getDestinyByDirecton(int directionId) {
        return destinyMapper.toDto(destinyRepository.findByDireciontId(directionId));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public DestinyDTO getDestinyByShape(int shapeId) {
        return destinyMapper.toDto(destinyRepository.findByShapeId(shapeId));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public DestinyDTO getDestinyByNumber(int numberId) {
        return destinyMapper.toDto(destinyRepository.findByNumber(numberId));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getShapeNames(Integer destinyId) {
        return shapeService.getShapesByDestiny(destinyId).stream()
                .map(ShapeDTO::getShape)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getColorNames(Integer destinyId) {
        return colorService.getColorsByDestiny(destinyId).stream()
                .map(ColorDTO::getColor)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getDirectionNames(Integer destinyId) {
        return directionService.getDirections(destinyId).stream()
                .map(DirectionDTO::getDirection)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<Integer> getNumberNames(Integer destinyId) {
        return numberService.getNumbers(destinyId).stream()
                .map(NumberDTO::getNumber)
                .collect(Collectors.toList());
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getAnimalNames(Integer destinyId, String tuongKhacTruoc, String tuongKhacSau) {
        List<ColorDTO> colors = colorService.getColorsByDestiny(destinyId);
        List<String> animalNames = new ArrayList<>();
        for (ColorDTO color : colors) {
            List<AnimalCategoryDTO> animalCategories = animalService.getAnimalCategoryByColorId(color.getId());
            for (AnimalCategoryDTO animalCategory : animalCategories) {
                List<Integer> animalDestinies = getAllDestinyByAnimal(animalCategory.getId()).stream()
                        .map(DestinyDTO::getId)
                        .toList();
                boolean hasConflict = false;
                for (Integer destinyItem : animalDestinies) {
                    if (destinyItem.equals(getDestinyId(tuongKhacTruoc).getId()) ||
                            destinyItem.equals(getDestinyId(tuongKhacSau).getId())) {
                        hasConflict = true;
                        break;
                    }
                }
                if (!hasConflict) {
                    animalNames.add(animalCategory.getAnimalCategoryName());
                }
            }
        }

        return animalNames;
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public List<String> getShelterNames(Integer destinyId) {
        List<ShapeDTO> shapes = shapeService.getShapesByDestiny(destinyId);
        List<String> shelterNames = new ArrayList<>();
        for (ShapeDTO shape : shapes) {
            List<ShelterCategoryDTO> shelters = shelterService.getAllSheltersByShape(shape.getId());
            for (ShelterCategoryDTO shelter : shelters) {
                shelterNames.add(shelter.getShelterCategoryName());
            }
        }
        return shelterNames;
    }

}
