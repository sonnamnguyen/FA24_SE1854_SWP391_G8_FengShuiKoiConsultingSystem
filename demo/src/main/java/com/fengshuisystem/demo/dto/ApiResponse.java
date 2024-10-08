package com.fengshuisystem.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int code = 1000;
    private String message;
    private T result;

    // Thuộc tính mới để chứa tên trường và thông báo lỗi
    private String field;
    private String fieldErrorMessage;

    // Phương thức setFieldError để lưu thông tin lỗi về trường
    public void setFieldError(String field, String errorMessage) {
        this.field = field;
        this.fieldErrorMessage = errorMessage;
    }
}
