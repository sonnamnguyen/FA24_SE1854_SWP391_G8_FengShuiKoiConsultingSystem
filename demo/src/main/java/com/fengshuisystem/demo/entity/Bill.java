package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor // Thêm dòng này để tạo constructor không tham số
@AllArgsConstructor // Nếu cần thêm constructor có tham số
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(nullable = false)
    private Account account;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(nullable = false)
    private Payment payment;

    @Column(name = "sub_amount")
    private Integer subAmount;

    @Column(name = "VAT")
    private Integer vat;

    @Column(name = "VAT_amount")
    private Integer vatAmount;

    @NotNull
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @Column(name = "created_by")
    private Instant createdBy;

    @ManyToMany(mappedBy = "bills", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Package> packageFields = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "consultation_request_id")
    private ConsultationRequest consultationRequest;

}