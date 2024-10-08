package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "package")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="package_id")
    Integer id;
    @Column(name="price",nullable = false)
    Double price;
    @Column(name="package_name",nullable=false)
    String packageName;
    @Column(name="description",nullable=false)
    String description;
    @Column(name = "duration",nullable = false)
    Integer duration;
    @Column(name = "create_date",nullable = false)
    LocalDate createDate;
    @Column(name = "update_date")
    LocalDate updateDate;
    @Column(name = "create_by",nullable = false)
    String createBy;
    @Column(name = "update_by")
    String updateBy;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    @JoinTable(
            name = "bill_package",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "bill_id")
    )
    Set<Bill> bills;

}
