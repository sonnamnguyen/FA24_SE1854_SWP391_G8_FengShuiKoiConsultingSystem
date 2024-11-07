
package com.fengshuisystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @NotNull(message = "Account must not be null")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;

    @NotNull(message = "Payment must not be null")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "sub_amount")
    private BigDecimal subAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BillStatus status = BillStatus.PENDING;

    @Column(name = "VAT")
    private BigDecimal vat;

    @Column(name = "VAT_amount")
    private BigDecimal vatAmount;

    @NotNull
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @Size(max = 50)
    @Column(name = "created_by",  length = 50)
    private String createdBy;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Size(max = 255)
    @Nationalized
    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToMany(mappedBy = "bills", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Package> packageFields = new LinkedHashSet<>();

    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "consultation_request_id", nullable = false)
    @JsonBackReference
    private ConsultationRequest consultationRequest;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdDate = now;
        this.updatedDate = now;
        this.createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        this.updatedBy = this.createdBy;
    }

    protected void onUpdate() {
        this.updatedDate = Instant.now();
        this.updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
