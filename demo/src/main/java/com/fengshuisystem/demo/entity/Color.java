
package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
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
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "destiny_id", nullable = false)
    private Destiny destiny;

    @Size(max = 50)
    @Nationalized
    @Column(name = "color", length = 50)
    private String color;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @Size(max = 50)
    @Nationalized
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_date")
    private Instant updatedDate = Instant.now();

    @Size(max = 50)
    @Nationalized
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @ManyToMany(mappedBy = "colors", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<AnimalCategory> animalCategories = new LinkedHashSet<>();

}
