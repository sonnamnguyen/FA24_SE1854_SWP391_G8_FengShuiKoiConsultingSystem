package com.fengshuisystem.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ConsultationShelterRequestDTO {
    private Integer packageId; // ID của gói tư vấn đã chọn
    private List<Integer> directionIds; // ID của các hướng phong thủy
    private List<String> shapes; // Hình dáng của hồ cá
    private String size; // Kích thước của hồ cá
    private String description; // Mô tả chi tiết
}
