package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.DestinyDTO;
import com.fengshuisystem.demo.dto.request.DestinyTuongKhac;
import com.fengshuisystem.demo.dto.request.DestinyTuongSinh;
import com.fengshuisystem.demo.entity.Destiny;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.DestinyMapper;
import com.fengshuisystem.demo.repository.DestinyRepository;
import com.fengshuisystem.demo.service.DestinyService;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DestinyServiceImpl implements DestinyService {
    DestinyRepository destinyRepository;
    DestinyMapper destinyMapper;
    List<String> tuongSinhList = Arrays.asList("KIM", "THỦY", "MỘC", "HỎA", "THỔ");
    List<String> tuongKhacList = Arrays.asList("KIM", "MỘC", "THỔ", "THỦY", "HỎA");
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<DestinyDTO> getAllDestiny() {
        return destinyMapper.toDto(destinyRepository.findAll());
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public DestinyDTO getAllDestinyTuongSinhAndTuongKhac(String destinyName) {
        // Tìm Destiny theo tên mệnh
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

    public String findTuongSinhTruoc(String destiny) {
        int index = tuongSinhList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongSinhList.get((index - 1 + tuongSinhList.size()) % tuongSinhList.size());
    }

    public String findTuongSinhSau(String destiny) {
        int index = tuongSinhList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongSinhList.get((index + 1) % tuongSinhList.size());
    }

    public String findTuongKhacTruoc(String destiny) {
        int index = tuongKhacList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongKhacList.get((index - 1 + tuongKhacList.size()) % tuongKhacList.size());
    }

    public String findTuongKhacSau(String destiny) {
        int index = tuongKhacList.indexOf(destiny);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid destiny element: " + destiny);
        }
        return tuongKhacList.get((index + 1) % tuongKhacList.size());
    }
}
