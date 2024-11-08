package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.dto.request.DestinyTuongKhac;
import com.fengshuisystem.demo.dto.request.DestinyTuongSinh;
import com.fengshuisystem.demo.entity.Destiny;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DestinyServiceImpl implements DestinyService {
    DestinyRepository destinyRepository;
    DestinyMapper destinyMapper;
    ShapeService shapeService;
    ColorService colorService;
    AnimalService animalService;
    ShelterService shelterService;

    // Bảng Can (0-9 tương ứng với Canh, Tân, Nhâm, Quý, Giáp, Ất, Bính, Đinh, Mậu, Kỷ)
    int[] HEAVENLY_STEM_VALUES = {4, 4, 5, 5, 1, 1, 2, 2, 3, 3};
    // Bảng Chi (0-11 tương ứng với Thân, Dậu, Tuất, Hợi, Tý, Sửu, Dần, Mão, Thìn, Tỵ, Ngọ, Mùi)
    int[] EARTHLY_BRANCH_VALUES = {1, 1, 2, 2, 0, 0, 1, 1, 2, 2, 0, 0};

    List<String> tuongSinhList  = Arrays.asList("METAL", "WATER", "WOOD", "FIRE", "EARTH");
    List<String> tuongKhacList  = Arrays.asList("METAL", "WOOD", "EARTH", "WATER", "FIRE");

    String[] ELEMENTS = {"METAL", "WATER", "FIRE", "EARTH", "WOOD"};


    @Override
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
    public DestinyDTO getDestinyId(String destinyName) {
        Destiny destiny = destinyRepository.findByDestiny(destinyName)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));
        return destinyMapper.toDto(destiny);
    }

    @Override
    public List<DestinyDTO> getAllDestinyByAnimal(int animalId) {
        return destinyRepository.findAllByAnimalId(animalId)
                .stream()
                .map(destinyMapper::toDto)
                .toList();
    }

    @Override
    public DestinyDTO getDestinyByDirection(int directionId) {
        return destinyMapper.toDto(destinyRepository.findByDirectionId(directionId));
    }

    @Override
    public DestinyDTO getDestinyByShape(int shapeId) {
        return destinyMapper.toDto(destinyRepository.findByShapeId(shapeId));
    }

    @Override
    public DestinyDTO getDestinyByNumber(int numberId) {
        return destinyMapper.toDto(destinyRepository.findByNumber(numberId));
    }



    @Override
    public List<AnimalCategoryDTO> getAnimal(Integer destinyId, String tuongKhacTruoc, String tuongKhacSau) {
        List<ColorDTO> colors = colorService.getColorsByDestiny(destinyId);
        List<AnimalCategoryDTO> animalList = new ArrayList<>();
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
                    animalList.add(animalCategory);
                }
            }
        }
        return animalList;
    }


    @Override
    public List<ShelterCategoryDTO> getShelter(Integer destinyId) {
        List<ShapeDTO> shapes = shapeService.getShapesByDestiny(destinyId);
        List<ShelterCategoryDTO> shelterList = new ArrayList<>();
        for (ShapeDTO shape : shapes) {
            List<ShelterCategoryDTO> shelters = shelterService.getAllSheltersByShape(shape.getId());
            shelterList.addAll(shelters);
        }
        return shelterList;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public DestinyDTO getAllDestinyTuongSinhAndTuongKhac(String destinyName) {
        Destiny destiny = destinyRepository.findByDestiny(destinyName)
                .orElseThrow(() -> new AppException(ErrorCode.DESTINY_NOT_EXISTED));

        // Map Destiny thành DestinyDTO
        DestinyDTO dto = destinyMapper.toDto(destiny);

        // Tìm danh sách tương sinh
        String tuongSinhTruoc = findTuongSinhTruoc(dto.getDestiny());
        String tuongSinhSau = findTuongSinhSau(dto.getDestiny());
        dto.setDestinyTuongSinhs(Arrays.asList(
                new DestinyTuongSinh(tuongSinhTruoc),
                new DestinyTuongSinh(tuongSinhSau)
        ));

        // Tìm danh sách tương khắc
        String tuongKhacTruoc = findTuongKhacTruoc(dto.getDestiny());
        String tuongKhacSau = findTuongKhacSau(dto.getDestiny());
        dto.setDestinyTuongKhacs(Arrays.asList(
                new DestinyTuongKhac(tuongKhacTruoc),
                new DestinyTuongKhac(tuongKhacSau)
        ));

        return dto;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<DestinyDTO> getAllDestiny() {
        return destinyMapper.toDto(destinyRepository.findAll());
    }
}
