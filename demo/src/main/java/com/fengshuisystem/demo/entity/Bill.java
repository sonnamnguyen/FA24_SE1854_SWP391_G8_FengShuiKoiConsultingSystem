package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bill_id")
    Integer id;
    @Column(name = "sub_amount")
    Double subAmount;
    @Column(name="VAT_amount",nullable = false)
    Double VATAmount;
    @Column(name="total_amount")
    Double totalAmount;
    @Column(name = "create_date",nullable = false)
    LocalDate createDate;
    @Column(name = "update_date")
    LocalDate updateDate;
    @Column(name = "create_by",nullable = false)
    String createBy;
    @Column(name = "update_by")
    String updateBy;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    @JoinTable(
            name = "bill_package",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id")
    )
    Set<Package> packageSet;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
