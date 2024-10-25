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
public class ShelterCategoryDTO {

    Integer id;
    String shelterCategoryName;

    // Nếu Shape có nhiều dữ liệu, chỉ giữ ID hoặc thông tin cần thiết
    ShapeDTO shape;

    // Kích thước và thông số của chỗ ở
    Double width;
    Double height;
    Double length;
    Double diameter;
    Double waterVolume;

    String waterFiltrationSystem;
    String description;

    // Trạng thái (có thể là Enum)
    Status status;

    // Thông tin ngày tạo và cập nhật
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    // Danh sách ảnh liên quan
    List<ShelterImageDTO> shelterImages;
}
