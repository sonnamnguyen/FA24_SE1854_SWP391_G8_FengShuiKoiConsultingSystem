package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "post_category")
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_category_id",nullable = false)
    Integer postCategoryId;
    @Column(name="post_category_name",nullable=false,length=255)
    String postCategoryName;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    @JoinTable(
            name = "post_postCategory",
            joinColumns = @JoinColumn(name = "post_category_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    Set<Post> post;
}
