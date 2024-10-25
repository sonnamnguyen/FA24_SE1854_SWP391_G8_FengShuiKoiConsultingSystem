package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimalCategoryDTO {

    Integer id;
    String animalCategoryName;
    String description;
    String origin;

    // Đổi status sang Enum nếu có tập giá trị cố định
    Status status;

    // Thông tin ngày tạo và cập nhật
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    // Danh sách ảnh liên quan
    List<AnimalImageDTO> animalImages;

    // Danh sách màu sắc liên quan
    List<ColorDTO> colors;
}
