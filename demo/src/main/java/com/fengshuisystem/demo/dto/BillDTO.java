package com.fengshuisystem.demo.dto;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.Payment;
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
    private Account account;
    private Payment payment;
    private Integer subAmount;
    private Integer vat;
    private String status;
    private Integer vatAmount;
    private BigDecimal totalAmount;
    private Instant createdDate = Instant.now();
    private String createdBy;
    private String updatedBy;
    private Instant updatedDate;
    private List<PackageDTO> packageFields;
}
