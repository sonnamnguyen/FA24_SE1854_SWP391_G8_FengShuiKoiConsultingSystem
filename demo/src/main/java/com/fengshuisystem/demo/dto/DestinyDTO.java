package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.dto.request.DestinyTuongKhac;
import com.fengshuisystem.demo.dto.request.DestinyTuongSinh;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DestinyDTO {
    Integer id;
    String destiny;
    List<DirectionDTO> directions;
    List<NumberDTO> numbers;
    List<DestinyTuongSinh> destinyTuongSinhs;
    List<DestinyTuongKhac> destinyTuongKhacs;
}