package com.fengshuisystem.demo.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResDTO implements Serializable {
    private String status;
    private String message;
    private String URL;
}
