package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "destiny_year")
public class DestinyYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destiny_year_id", nullable = false)
    private Integer id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "destiny", length = 50)
    private String destiny;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdDate;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedDate;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

}
