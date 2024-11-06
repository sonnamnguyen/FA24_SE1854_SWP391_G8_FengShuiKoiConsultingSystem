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
    INVALID_DOB(1007, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1008, "Invalid credentials, please try again.", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1009, "Password existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1010, "Role not existed", HttpStatus.NOT_FOUND),
    ANIMAL_EXISTED(1011, "Animal existed", HttpStatus.BAD_REQUEST),
    ANIMAL_NOT_EXISTED(1012, "Animal not existed", HttpStatus.NOT_FOUND),
    SHELTER_EXISTED(1013, "Shelter existed", HttpStatus.BAD_REQUEST),
    SHELTER_NOT_EXISTED(1014, "Shelters not existed", HttpStatus.NOT_FOUND),
    COLOR_NOT_EXISTED(1015, "Colors not existed", HttpStatus.NOT_FOUND),
    COLOR_EXISTED(1016, "Colors existed", HttpStatus.BAD_REQUEST),
    DESTINY_NOT_EXISTED(1017, "Destiny not existed", HttpStatus.NOT_FOUND),
    ERROR_CODE(1018, "Activate Code not true", HttpStatus.BAD_REQUEST),
    USER_INACTIVE(1019, "User is inactive", HttpStatus.BAD_REQUEST),
    CONSULATION_RESULT_NOT_EXISTED(1020, "Consulation result not existed", HttpStatus.NOT_FOUND),
    SHAPE_NOT_EXISTED(1021, "Shape not existed", HttpStatus.NOT_FOUND),
    CONSULATION_CATEGORY_NOT_EXISTED(1022, "Consulation Category not existed", HttpStatus.NOT_FOUND),
    POST_NOT_EXISTED(2001, "Post not existed", HttpStatus.NOT_FOUND),
    COMMENT_NOT_EXISTED(2002, "Comment not existed", HttpStatus.NOT_FOUND),
    POST_IMAGE_NOT_EXISTED(2003, "Post image not existed", HttpStatus.NOT_FOUND),
    POST_CATEGORY_NOT_EXISTED(2004, "post category not existed", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_EXISTED(2005,"payment not existed", HttpStatus.NOT_FOUND),
    BILL_NOT_EXISTED(2006,"bill not existed", HttpStatus.NOT_FOUND),
    PACKAGE_NOT_EXISTED(2007,"package not existed", HttpStatus.NOT_FOUND),
    INVALID_BILL_STATUS(2008,"invalid bill status", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_FOUND(2009, "request not found", HttpStatus.NOT_FOUND),
    CONSULTATION_REQUEST_DETAIL_NOT_FOUND(2010, "consultation request detail not found", HttpStatus.NOT_FOUND),
    CONSULTATION_REQUEST_DETAIL_NOT_COMPLETED(2011, "consultation request detail not completed", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(2011, "payment not found", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
