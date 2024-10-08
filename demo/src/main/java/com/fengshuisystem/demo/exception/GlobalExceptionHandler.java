package com.fengshuisystem.demo.exception;

import com.fengshuisystem.demo.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Các thuộc tính cho ràng buộc min và max
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max"; // Bổ sung thêm xử lý max

    // Xử lý ngoại lệ chung cho tất cả exception
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception); // Ghi log lỗi chi tiết
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Xử lý ngoại lệ AppException tùy chỉnh
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // Xử lý ngoại lệ khi bị từ chối quyền truy cập
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    // Xử lý ngoại lệ liên quan đến validation (kiểm tra ràng buộc dữ liệu)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        var fieldError = exception.getFieldError(); // Lấy lỗi đầu tiên
        String enumKey = fieldError.getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {
            // Bổ sung ghi log chi tiết hơn nếu xảy ra IllegalArgumentException
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info("Validation attributes: {}", attributes); // Ghi log chi tiết về thuộc tính validation

        } catch (IllegalArgumentException e) {
            log.warn("Could not map error code: {}", enumKey); // Log nếu không map được mã lỗi
        }

        ApiResponse apiResponse = new ApiResponse();

        // Gán mã lỗi và thông báo lỗi với dữ liệu phản hồi chi tiết hơn
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        // Bổ sung thông tin chi tiết về trường bị lỗi và lý do
        apiResponse.setFieldError(fieldError.getField(), fieldError.getDefaultMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Thay thế placeholder {min} và {max} trong thông báo lỗi với giá trị thực tế
    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE)); // Xử lý min
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE)); // Xử lý max

        // Thay thế các placeholder {min} và {max} nếu có
        message = message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
        if (attributes.containsKey(MAX_ATTRIBUTE)) {
            message = message.replace("{" + MAX_ATTRIBUTE + "}", maxValue);
        }

        return message;
    }
}
