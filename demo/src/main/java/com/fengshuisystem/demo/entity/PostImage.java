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
@Table(name = "post_image")
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id",nullable = false)
    Integer postImageId;
    @Column(name = "image_name",nullable = false,length = 255)
    String imageName;
    @Column(name = "image_url",nullable = false,length = 255)
    String imageUrl;
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
