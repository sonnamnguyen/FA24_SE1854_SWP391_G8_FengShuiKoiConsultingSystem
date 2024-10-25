package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class AnimalCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_category_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "animal_category_name", nullable = false)
    private String animalCategoryName;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 255)
    @Column(name = "origin")
    private String origin;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Size(max = 255)
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_date")
    private Instant updatedDate = Instant.now();

    @Size(max = 255)
    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "animalCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<AnimalImage> animalImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "animalCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ConsultationAnimal> consultationAnimals = new LinkedHashSet<>();

    // Chuyển từ OneToMany sang ManyToMany với ConsultationRequestDetail
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "consultation_animal_category",
            joinColumns = @JoinColumn(name = "animal_category_id"),
            inverseJoinColumns = @JoinColumn(name = "consultation_request_detail_id")
    )
    private Set<ConsultationRequestDetail> consultationRequestDetails = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "animal_color",
            joinColumns = @JoinColumn(name = "animal_category_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private Set<Color> colors = new LinkedHashSet<>();
}
