package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "consultation_destiny")
public class ConsultationDestiny {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_destiny_id", nullable = false)
    private Integer id;

    @Column(name = "destiny", columnDefinition = "nvarchar(10)")
    private String destiny;

    @Column(name = "code", length = 255)
    private String code;

    @Column(name = "name", length = 255, columnDefinition = "nvarchar(100)")
    private String name;

    @Column(name = "min_value")
    private Integer minValue;

    @Column(name = "max_value")
    private Integer maxValue;

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

