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
                .message("Dựa trên mệnh của bạn, hồ cá Koi này sẽ mang lại sự hài hòa, ổn định và cân bằng lâu dài. Những yếu tố tương hợp được lựa chọn kỹ càng để đảm bảo phong thủy tốt nhất, giúp thu hút năng lượng tích cực, đồng thời giữ cho gia đình và sự nghiệp của bạn luôn vững vàng.")
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
                .message("Với sự tương sinh từ yếu tố phong thủy, hồ cá Koi này sẽ mang đến sự phát triển vượt bậc cho bạn. Những yếu tố phong thủy tương sinh giúp tăng cường năng lượng, hỗ trợ thăng tiến sự nghiệp và thu hút tài lộc, đồng thời bảo vệ gia đình bạn trước những khó khăn và trở ngại. Với sự lựa chọn kỹ lưỡng, hồ Koi sẽ trở thành nguồn động lực mạnh mẽ, thúc đẩy mọi khía cạnh trong cuộc sống của bạn.")
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
