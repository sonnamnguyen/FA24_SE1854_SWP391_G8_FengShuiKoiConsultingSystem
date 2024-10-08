package com.fengshuisystem.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_comment_id",nullable = false)
    Integer postCommentId;
    @Column(name = "content", length = 255,nullable = false)
    String content;
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
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
