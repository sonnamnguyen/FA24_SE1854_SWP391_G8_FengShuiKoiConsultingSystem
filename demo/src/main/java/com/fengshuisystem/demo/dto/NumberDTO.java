package com.fengshuisystem.demo.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NumberDTO {
    Integer id;
    @JsonIgnore
    Integer destinyId;
    Integer number;
}
