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
    INVALID_DOB(1007, "Your age must be at least {min}", HttpStatus.BAD_REQUEST), // merged from both sources
    INVALID_CREDENTIALS(1008, "Invalid credentials, please try again.", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1009, "Password existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1010, "Role not existed", HttpStatus.NOT_FOUND),
    ANIMAL_EXISTED(1011, "The animal already exists. Please choose a different name.", HttpStatus.BAD_REQUEST),
    ANIMAL_NOT_EXISTED(1012, "Animal not found in the system. Please verify the name or select another.", HttpStatus.NOT_FOUND),
    SHELTER_EXISTED(1013, "This shelter name is already taken. Please select a unique name.", HttpStatus.BAD_REQUEST),
    SHELTER_NOT_EXISTED(1014, "The specified shelter does not exist in the system. Please check and try again.", HttpStatus.NOT_FOUND),
    COLOR_NOT_EXISTED(1015, "Specified colors do not exist. Verify your color selection and try again.", HttpStatus.NOT_FOUND),
    COLOR_EXISTED(1016, "The selected color already exists. Please choose different colors.", HttpStatus.BAD_REQUEST),
    DESTINY_NOT_EXISTED(1017, "Destiny not existed. Please choose destiny and try again", HttpStatus.NOT_FOUND),
    ERROR_CODE(1018, "Activate Code not true", HttpStatus.BAD_REQUEST),
    USER_INACTIVE(1019, "User is inactive", HttpStatus.BAD_REQUEST),
    CONSULATION_RESULT_NOT_EXISTED(1020, "Consulation result not existed", HttpStatus.NOT_FOUND),
    SHAPE_NOT_EXISTED(1021, "No shape selected. Please choose a shape before proceeding.", HttpStatus.NOT_FOUND),
    CONSULATION_CATEGORY_NOT_EXISTED(1022, "Consulation Category not existed", HttpStatus.NOT_FOUND),
    SHAPE_EXISTED(1023, "Shape existed", HttpStatus.BAD_REQUEST), // added from second file
    CONSULTATION_CATEGORY_NOT_EXISTED(1024, "Consulation Category not existed", HttpStatus.NOT_FOUND),
    CONSULTATION_SHELTER_DOES_NOT_EXIST(1025, "Consulation shelter does not exist", HttpStatus.NOT_FOUND),
    CONSULTATION_ANIMAL_DOES_NOT_EXIST(1026, "Consulation animal does not exist", HttpStatus.NOT_FOUND),
    POST_NOT_EXISTED(2001, "Post not existed", HttpStatus.NOT_FOUND),
    COMMENT_NOT_EXISTED(2002, "Comment not existed", HttpStatus.NOT_FOUND),
    POST_IMAGE_NOT_EXISTED(2003, "Post image not existed", HttpStatus.NOT_FOUND),
    POST_CATEGORY_NOT_EXISTED(2004, "Post category not existed", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_EXISTED(2005, "Payment not existed", HttpStatus.NOT_FOUND),
    BILL_NOT_EXISTED(2006, "Bill not existed", HttpStatus.NOT_FOUND),
    PACKAGE_NOT_EXISTED(2007, "Package not existed", HttpStatus.NOT_FOUND),
    INVALID_BILL_STATUS(2008, "Invalid bill status", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_FOUND(2009, "Request not found", HttpStatus.NOT_FOUND),
    CONSULTATION_REQUEST_DETAIL_NOT_FOUND(2010, "Consultation request detail not found", HttpStatus.NOT_FOUND),
    CONSULTATION_REQUEST_DETAIL_NOT_COMPLETED(2011, "Consultation request detail not completed", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(2012, "Payment not found", HttpStatus.BAD_REQUEST), // corrected duplicate with unique code
    TOO_MANY_COLORS(2013, "A maximum of three colors is allowed. Please reduce the selection.", HttpStatus.BAD_REQUEST),
    PICK_SAME_IMAGE(2014, "Duplicate image URLs detected in the request. Each image URL must be unique.", HttpStatus.BAD_REQUEST),
    IMAGE_ALREADY_EXISTED(2015, "This image URL is already in use. Please choose a different image URL.", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_FOUND(2016, "No image found for the given URL. Please check the URL and try again.", HttpStatus.NOT_FOUND),
    NONE_DATA_ANIMAL(2017, "No animal data found for the given search. Please verify the details and try again.", HttpStatus.NOT_FOUND),
    NONE_DATA_SHELTER(2018, "No shelter data found for the given search. Please verify the details and try again.", HttpStatus.NOT_FOUND),
    NONE_DATA_COLOR(2019, "No color data found for the given search. Please verify the details and try again.", HttpStatus.NOT_FOUND),
    NONE_DATA_SHAPE(2020, "No shape data found for the given search. Please verify the details and try again.", HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
