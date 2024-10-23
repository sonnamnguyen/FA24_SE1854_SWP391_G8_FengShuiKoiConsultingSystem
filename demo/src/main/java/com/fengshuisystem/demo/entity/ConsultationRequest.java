package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ConsultationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private Account account;

    // Xóa CascadeType.DETACH để tránh việc Hibernate tự động tách Package ra khỏi context.
//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @NotNull
    // thêm (name = "package_id")
    @JoinColumn(name = "package_id")
    private Package packageField;

    @Size(max = 1000)
    @NotNull
    @Nationalized
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @NotNull
    @Column(name = "created_date", nullable = false)
    @ColumnDefault("GETDATE()")  // SQL Server default for datetime
    private Instant createdDate = Instant.now();

    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "created_by", nullable = false, length = 300)
    @ColumnDefault("'SYSTEM'")
    private String createdBy = "system";

    // updateted -> updated
    @NotNull
    @Column(name = "updated_date", nullable = false)
    @ColumnDefault("GETDATE()")
    private Instant updatedDate = Instant.now();

    // updateted -> updated
    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "updated_by", nullable = false, length = 300)
    @ColumnDefault("'SYSTEM'")  // Default to 'SYSTEM'
    private String updatedBy = "system";

    // Lifecycle hook to set default values on insert
    @PrePersist
    protected void onCreate() {
        this.createdDate = Instant.now();
        this.updatedDate = Instant.now();
    }

    // Lifecycle hook to update the 'updatedDate' field on update
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Instant.now();
    }

    @OneToMany(mappedBy = "requestDetail", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ConsultationRequestDetail> consultationRequestDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "request", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ConsultationResult> consultationResults = new LinkedHashSet<>();

}