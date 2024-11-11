package com.fengshuisystem.demo.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.Payment;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillDTO {
    private Integer id;

    @JsonIgnore
    private Account account;

    private PaymentDTO payment;
    private BigDecimal subAmount;
    private BigDecimal vat;
    private BillStatus status;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private Instant createdDate = Instant.now();
    private String createdBy;
    private String updatedBy;
    private Instant updatedDate;

    private Integer consultationRequestId;

    private List<PackageDTO> packageFields;

    private Integer accountId;
    private Integer paymentId;
}
