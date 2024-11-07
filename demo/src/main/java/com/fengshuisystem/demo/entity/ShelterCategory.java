package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ShelterCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelter_category_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "shelter_category_name", nullable = false)
    private String shelterCategoryName;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "shape_id", nullable = false)
    private Shape shape;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

    @Column(name = "length")
    private Double length;

    @Column(name = "diameter")
    private Double diameter;

    @Column(name = "water_volume")
    private Double waterVolume;

    @Size(max = 255)
    @Nationalized
    @Column(name = "water_filtration_system")
    private String waterFiltrationSystem;

    @Size(max = 1000)
    @Nationalized
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Size(max = 255)
    @Nationalized
    @Column(name = "updated_by")
    private String updatedBy;

    // Chuyển từ OneToMany sang ManyToMany với ConsultationRequestDetail
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "consultation_shelter_category",
            joinColumns = @JoinColumn(name = "shelter_category_id"),
            inverseJoinColumns = @JoinColumn(name = "consultation_request_detail_id")
    )
    private Set<ConsultationRequestDetail> consultationRequestDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shelterCategory", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ConsultationShelter> consultationShelters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shelterCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ShelterImage> shelterImages = new LinkedHashSet<>();
}
