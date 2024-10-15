package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ConsultationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_result_id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private ConsultationRequest request;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private ConsultationRequestDetail requestDetail;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "consultation_category_id")
    private ConsultationCategory consultationCategory;

    @Column(name = "consultation_date")
    private Instant consultationDate;

    @Size(max = 500)
    @Nationalized
    @Column(name = "consultant_name", length = 500)
    private String consultantName;

    @NotNull
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

    @OneToMany(mappedBy = "consultation", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ConsultationAnimal> consultationAnimals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "consultation", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ConsultationShelter> consultationShelters = new LinkedHashSet<>();

}