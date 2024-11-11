package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "consultation_animal")
public class ConsultationAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_animal_id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "consultation_result_id")
    private ConsultationResult consultationResult;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "animal_category_id")
    private AnimalCategory animalCategory;

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

    @ManyToMany
    @JoinTable(
            name = "consultation_animal_numbers",
            joinColumns = @JoinColumn(name = "consultation_animal_id"),
            inverseJoinColumns = @JoinColumn(name = "number_id")
    )
    private Set<Number> numbers;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Request status = Request.CANCELLED;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 300)
    @Column(name = "created_by", length = 300)
    private String createdBy;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Size(max = 300)
    @Column(name = "updated_by", length = 300)
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
