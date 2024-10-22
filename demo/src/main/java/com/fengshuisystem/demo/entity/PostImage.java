
package com.fengshuisystem.demo.entity;

import com.fengshuisystem.demo.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(nullable = false)
    private Post post;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Lob
    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

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

}
