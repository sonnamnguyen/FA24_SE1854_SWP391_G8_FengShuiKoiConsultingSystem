
package com.fengshuisystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Integer id;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private Account account;
    @JsonIgnore
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(nullable = false)
    private PostCategory postCategory;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Package packageField;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private Destiny destiny;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "like_number", nullable = false)
    private Integer likeNumber;

    @NotNull
    @Column(name = "dislike_number", nullable = false)
    private Integer dislikeNumber;

    @NotNull
    @Column(name = "share_number", nullable = false)
    private Integer shareNumber;

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
    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Comment> comments = new LinkedHashSet<>();
    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<PostImage> postImages = new LinkedHashSet<>();

}
