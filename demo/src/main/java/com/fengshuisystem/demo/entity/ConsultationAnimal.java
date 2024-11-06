
package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.ConsulationAnimalStatus;
import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
public class ConsultationAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_animal_id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private ConsultationResult consultation;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private AnimalCategory animalCategory;

    @Size(max = 500)
    @Nationalized
    @Column(name = "description", length = 500)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "consultation_animal_numbers",
            joinColumns = @JoinColumn(name = "consultation_animal_id"),
            inverseJoinColumns = @JoinColumn(name = "number_id")
    )
    private Set<Number> numbers;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @Size(max = 300)
    @Column(name = "created_by", length = 300)
    private String createdBy;

    @Column(name = "updateted_date")
    private Instant updatetedDate = Instant.now();

    @Size(max = 300)
    @Column(name = "updateted_by", length = 300)
    private String updatetedBy;

}