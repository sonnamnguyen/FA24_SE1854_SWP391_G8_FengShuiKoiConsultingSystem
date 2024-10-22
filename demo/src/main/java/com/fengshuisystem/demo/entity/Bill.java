package com.fengshuisystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BillStatus status = BillStatus.PENDING;

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
    private String createdBy;
    @Column(name = "updated_date")
    private Instant updatedDate;

    @Size(max = 255)
    @Nationalized
    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToMany(mappedBy = "bills", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Package> packageFields = new LinkedHashSet<>();

}