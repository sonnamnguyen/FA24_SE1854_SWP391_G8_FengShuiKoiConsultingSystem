package com.fengshuisystem.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Destiny2Response {
    String Destiny;
    List<String> huong;
    List<String> mau;
    List<String> so;
}
