package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.dto.response.AutoConsultationResponse;
import com.fengshuisystem.demo.dto.response.AutoConsultationContainerResponse;
import com.fengshuisystem.demo.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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

        String tuongKhacTruoc = destinyService.findTuongKhacTruoc(destiny);
        String tuongKhacSau = destinyService.findTuongKhacSau(destiny);
        DestinyDTO tuongHopId = destinyService.getDestinyId(destiny);
        return AutoConsultationResponse.builder()
                .message("Dựa trên mệnh của bạn, hồ cá Koi này sẽ mang lại sự hài hòa, ổn định và cân bằng lâu dài. Những yếu tố tương hợp được lựa chọn kỹ càng để đảm bảo phong thủy tốt nhất, giúp thu hút năng lượng tích cực, đồng thời giữ cho gia đình và sự nghiệp của bạn luôn vững vàng.")
                .numbers(numberService.getNumbers(tuongHopId.getId()).stream().map(NumberDTO::getNumber).toList())
                .directions(directionService.getDirections(tuongHopId.getId()).stream().map(DirectionDTO::getDirection).toList())
                .shapes(shapeService.getShapesByDestiny(tuongHopId.getId()).stream().map(ShapeDTO::getShape).toList())
                .colors(colorService.getColorsByDestiny(tuongHopId.getId()).stream().map(ColorDTO::getColor).toList())
                .shelters(destinyService.getShelterNames(tuongHopId.getId()))
                .animals(destinyService.getAnimalNames(tuongHopId.getId(), tuongKhacTruoc, tuongKhacSau))
                .build();
    }

    @Override
    public AutoConsultationResponse autoConsultationVipPro2(int year) {
        String destiny = destinyService.getDestinyFromYear(year);

        String tuongSinhTruoc = destinyService.findTuongSinhTruoc(destiny);
        String tuongKhacTruoc = destinyService.findTuongKhacTruoc(destiny);
        String tuongKhacSau = destinyService.findTuongKhacSau(destiny);

        DestinyDTO tuongSinhId = destinyService.getDestinyId(tuongSinhTruoc);

        return AutoConsultationResponse.builder()
                .message("Với sự tương sinh từ yếu tố phong thủy, hồ cá Koi này sẽ mang đến sự phát triển vượt bậc cho bạn. Những yếu tố phong thủy tương sinh giúp tăng cường năng lượng, hỗ trợ thăng tiến sự nghiệp và thu hút tài lộc, đồng thời bảo vệ gia đình bạn trước những khó khăn và trở ngại. Với sự lựa chọn kỹ lưỡng, hồ Koi sẽ trở thành nguồn động lực mạnh mẽ, thúc đẩy mọi khía cạnh trong cuộc sống của bạn.")
                .numbers(numberService.getNumbers(tuongSinhId.getId()).stream().map(NumberDTO::getNumber).toList())
                .directions(directionService.getDirections(tuongSinhId.getId()).stream().map(DirectionDTO::getDirection).toList())
                .shapes(shapeService.getShapesByDestiny(tuongSinhId.getId()).stream().map(ShapeDTO::getShape).toList())
                .colors(colorService.getColorsByDestiny(tuongSinhId.getId()).stream().map(ColorDTO::getColor).toList())
                .shelters(destinyService.getShelterNames(tuongSinhId.getId()))
                .animals(destinyService.getAnimalNames(tuongSinhId.getId(), tuongKhacTruoc, tuongKhacSau))
                .build();
    }

    @Override
    public AutoConsultationContainerResponse autoConsultationResponseContainer(int year){
        return AutoConsultationContainerResponse.builder()
                .consultation1(autoConsultationVipPro2(year))
                .consultation2(autoConsultationVipPro(year))
                .build();
    }
}
