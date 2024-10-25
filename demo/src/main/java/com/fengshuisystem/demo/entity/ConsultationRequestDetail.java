package com.fengshuisystem.demo.entity;

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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class ConsultationRequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_request_detail_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "consultation_request_id", nullable = false)
    private ConsultationRequest consultationRequest;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "shelter_category_id")
    private ShelterCategory shelterCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "animal_category_id")
    private AnimalCategory animalCategory;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Request status = Request.PENDING; //

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "created_by", nullable = false, length = 300)
    private String createdBy;

    @NotNull
    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "updated_by", nullable = false, length = 300)
    private String updatedBy;

    @Size(max = 1000)
    @NotNull
    @Nationalized
    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @OneToMany(mappedBy = "requestDetail", cascade = CascadeType.ALL)
    private Set<ConsultationResult> consultationResults = new LinkedHashSet<>();

    @Getter
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "consultation_shelter_category",
            joinColumns = @JoinColumn(name = "consultation_request_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "shelter_category_id")
    )
    private Set<ShelterCategory> shelterCategories = new LinkedHashSet<>();

    @Getter
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "consultation_animal_category",
            joinColumns = @JoinColumn(name = "consultation_request_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_category_id")
    )
    private Set<AnimalCategory> animalCategories = new LinkedHashSet<>();

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdDate = now;
        this.updatedDate = now;

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        this.createdBy = currentUser != null ? currentUser : "SYSTEM"; // Đảm bảo có giá trị mặc định
        this.updatedBy = this.createdBy;

        if (this.status == null) {  // Đảm bảo không để status là null
            this.status = Request.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Instant.now();

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        this.updatedBy = currentUser != null ? currentUser : "SYSTEM"; // Đảm bảo có giá trị mặc định

    }

}
