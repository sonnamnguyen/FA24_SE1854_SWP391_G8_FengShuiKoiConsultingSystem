package com.fengshuisystem.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ConsultationAnimalRequestDTO {
    private Integer packageId; // ID của gói tư vấn đã chọn
    private List<Integer> animalIds; // ID của các loài cá đã chọn
    private List<Integer> colorIds; // ID của màu sắc của các loài cá
    private List<Integer> quantity; // Số lượng mỗi loại cá
    private String description; // Mô tả chi tiết
}
