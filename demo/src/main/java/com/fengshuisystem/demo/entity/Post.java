package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    Integer id;

    @Column(name = "content", length = 5000)
    String content;
    @Column(name = "like_number",nullable = false)
    Integer likeNumber;
    @Column(name = "dislike_number",nullable = false)
    Integer dislikeNumber;
    @Column(name = "share_number", nullable = false)
    Integer shareNumber;
    @Column(name = "status",nullable = false)
    Boolean status;
    @Column(name = "create_date",nullable = false)
    LocalDate createDate;
    @Column(name = "update_date")
    LocalDate updateDate;
    @Column(name = "create_by",nullable = false)
    String createBy;
    @Column(name = "update_by")
    String updateBy;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    @JoinTable(
            name = "post_postCategory",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "post_category_id")
    )
    Set<PostCategory> PostCategory;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostComment> postComments;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostImage> postImages;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
