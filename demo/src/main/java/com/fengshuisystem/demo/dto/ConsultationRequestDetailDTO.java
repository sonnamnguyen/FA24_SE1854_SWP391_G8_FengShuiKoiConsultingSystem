package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.entity.enums.Request;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationRequestDetailDTO {

    // ID của chi tiết yêu cầu tư vấn
    Integer id;

    // Danh sách các ID của ShelterCategory và AnimalCategory
    private List<Integer> shelterCategoryIds;
    private List<Integer> animalCategoryIds;

    // Mô tả chi tiết yêu cầu tư vấn
    String description;

    // Trạng thái của yêu cầu (nên là Enum)
    Request status;

    // Thông tin về thời gian tạo và cập nhật
    Instant createdDate;
    String createdBy;
    Instant updatedDate;
    String updatedBy;

    // Danh sách kết quả tư vấn liên quan
    List<ConsultationResultDTO> consultationResults;
}
