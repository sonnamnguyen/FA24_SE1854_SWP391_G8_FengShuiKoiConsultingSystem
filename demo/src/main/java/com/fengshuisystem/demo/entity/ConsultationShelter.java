package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.entity.enums.Status;
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
@Table(name = "consultation_shelter")
public class ConsultationShelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_shelter_id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "consultation_result_id")
    private ConsultationResult consultationResult;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "shelter_category_id")
    private ShelterCategory shelterCategory;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "consultation_shelter_direction",
            joinColumns = @JoinColumn(name = "consultation_shelter_id"),
            inverseJoinColumns = @JoinColumn(name = "direction_id")
    )
    private Set<Direction> directions = new LinkedHashSet<>();

    @NotNull
    @NotBlank
    @Nationalized
    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @SuppressWarnings("unused")
    @AssertTrue(message = "The description must contain at least 20 words.")
    public boolean isDescriptionValid() {
        if (description == null) {
            return false;
        }
        // Tách `description` thành các từ bằng regex
        String[] words = description.trim().split("\\s+");
        return words.length >= 20;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Request status = Request.CANCELLED;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "created_by", nullable = false, length = 300)
    private String createdBy;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @Size(max = 300)
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
}
