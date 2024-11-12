package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Destiny {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destiny_id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Nationalized
    @Column(name = "destiny", length = 10)
    private String destiny;

    @OneToMany(mappedBy = "destiny", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Color> colors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "destiny", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Direction> directions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "destiny", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Number> numbers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "destiny", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "destiny", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Shape> shapes = new LinkedHashSet<>();

}
