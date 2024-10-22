package com.fengshuisystem.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1009, "Invalid credentials, please try again.", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1010, "Password existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1011, "Role not existed", HttpStatus.NOT_FOUND),
    ANIMAL_EXISTED(1012, "Animal existed", HttpStatus.BAD_REQUEST),
    ANIMAL_NOT_EXISTED(1013, "Animal not existed", HttpStatus.NOT_FOUND),
    SHELTER_EXISTED(1014, "Shelter existed", HttpStatus.BAD_REQUEST),
    SHELTER_NOT_EXISTED(1015, "Shelters not existed", HttpStatus.NOT_FOUND),
    COLOR_NOT_EXISTED(1016, "Colors not existed", HttpStatus.NOT_FOUND),
    COLOR_EXISTED(1017, "Colors existed", HttpStatus.BAD_REQUEST),
    DESTINY_NOT_EXISTED(1018, "Destiny not existed", HttpStatus.NOT_FOUND),
    ERROR_CODE(1019, "Activate Code not true", HttpStatus.BAD_REQUEST),
    USER_INACTIVE(1020, "User is inactive", HttpStatus.BAD_REQUEST),
    CONSULATION_RESULT_NOT_EXISTED(1021, "Consulation result not existed", HttpStatus.NOT_FOUND),
    SHAPE_NOT_EXISTED(1022, "Shape not existed", HttpStatus.NOT_FOUND),
    CONSULATION_CATEGORY_NOT_EXISTED(1023, "Consulation Category not existed", HttpStatus.NOT_FOUND),
    POST_NOT_EXISTED(2001, "Post not existed", HttpStatus.NOT_FOUND),
    COMMENT_NOT_EXISTED(2002, "Comment not existed", HttpStatus.NOT_FOUND),
    POST_IMAGE_NOT_EXISTED(2003, "Post image not existed", HttpStatus.NOT_FOUND),
    POST_CATEGORY_NOT_EXISTED(2004, "post category not existed", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_EXISTED(2006,"payment not existed", HttpStatus.NOT_FOUND),
    BILL_NOT_EXISTED(2007,"bill not existed", HttpStatus.NOT_FOUND),
    PACKAGE_NOT_EXISTED(2007,"package not existed", HttpStatus.NOT_FOUND),;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
