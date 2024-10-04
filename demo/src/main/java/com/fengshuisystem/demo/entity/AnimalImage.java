package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class AnimalImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_image_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "animal_category_id")
    private AnimalCategory animalCategory;

    @Lob
    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_date")
    private Instant updatedDate = Instant.now();

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

}