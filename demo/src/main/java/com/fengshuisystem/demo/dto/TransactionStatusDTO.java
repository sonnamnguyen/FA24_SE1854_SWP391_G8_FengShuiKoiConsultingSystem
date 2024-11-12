package com.fengshuisystem.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionStatusDTO implements Serializable {
    private String status;
    private String message;
    private String data;
}
