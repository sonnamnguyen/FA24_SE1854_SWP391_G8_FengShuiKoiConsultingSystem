package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationRequestDTO {

    // ID của yêu cầu tư vấn
    Integer id;

    // Thông tin cơ bản về tài khoản người dùng đã đăng nhập (UserResponse thay vì Account)
    UserResponse account;

    // ID của gói dịch vụ được chọn
    Integer packageId;

    // Mô tả yêu cầu tư vấn
    String description;

    // Trạng thái của yêu cầu (nên chuyển thành Enum)
    String status;

    // Thông tin ngày tạo và người tạo
    Instant createdDate;
    String createdBy;

    // Thông tin ngày cập nhật và người cập nhật
    Instant updatedDate;
    String updatedBy;

    // Danh sách chi tiết yêu cầu tư vấn
    List<ConsultationRequestDetailDTO> consultationRequestDetails;

    // Danh sách kết quả tư vấn liên quan
    List<ConsultationResultDTO> consultationResults;
}
