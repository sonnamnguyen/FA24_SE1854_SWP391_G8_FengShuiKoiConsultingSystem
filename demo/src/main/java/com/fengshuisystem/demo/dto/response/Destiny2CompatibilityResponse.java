package com.fengshuisystem.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Destiny2CompatibilityResponse {
    String huong;
    Integer huongScore;
    String so;
    Integer soScore;
    String mau;
    Integer mauScore;
}
