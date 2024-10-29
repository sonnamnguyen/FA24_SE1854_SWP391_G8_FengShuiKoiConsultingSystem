package com.fengshuisystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fengshuisystem.demo.dto.response.UserResponse;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ConsultationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "package_id", nullable = false)
    private Package packageId;

    @Size(max = 1000)
    @Nationalized
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Request status = Request.PENDING;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @NotNull
    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate = Instant.now();

    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "created_by", nullable = false, length = 300)
    private String createdBy;

    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "updated_by", nullable = false, length = 300)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdDate = now;
        this.updatedDate = now;
        this.createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        this.updatedBy = this.createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Instant.now();
        this.updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @OneToMany(mappedBy = "consultationRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ConsultationRequestDetail> consultationRequestDetails = new HashSet<>();

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ConsultationResult> consultationResults = new HashSet<>();


    @OneToMany(mappedBy = "consultationRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Bill> bills = new HashSet<>();
}
