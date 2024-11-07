
package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Number {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "destiny_id")
    private Destiny destiny;

    @Column(name = "number")
    private Integer number;

    @ManyToMany (mappedBy = "numbers", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ConsultationAnimal> consultationAnimals = new LinkedHashSet<>();

}
