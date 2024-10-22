package com.fengshuisystem.demo.dto;
import com.fengshuisystem.demo.entity.Bill;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDTO {
    private Integer id;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private Instant createdDate;
    private String createdBy;
    private Instant updatedDate;
    private String updatedBy;
}
