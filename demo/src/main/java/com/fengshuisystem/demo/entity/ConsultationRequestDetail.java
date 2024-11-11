package com.fengshuisystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.entity.enums.Request;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.LinkedHashSet;
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
    @JsonIgnore
    private ConsultationRequest consultationRequest;

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

    @NotNull
    @NotBlank
    @Nationalized
    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    // Phương thức kiểm tra số từ có đủ ít nhất 100 từ
    @SuppressWarnings("unused")
    @AssertTrue(message = "The description must contain at least 100 words.")
    public boolean isDescriptionValid() {
        if (description == null) {
            return false;
        }
        // Tách `description` thành các từ bằng regex
        String[] words = description.trim().split("\\s+");
        return words.length >= 100;
    }

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
        this.createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        this.updatedBy = this.createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Instant.now();
        this.updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
