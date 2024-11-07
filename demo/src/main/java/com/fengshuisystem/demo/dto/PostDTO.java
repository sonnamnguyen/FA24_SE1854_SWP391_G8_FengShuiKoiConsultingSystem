package com.fengshuisystem.demo.dto;

import com.fengshuisystem.demo.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {
    private Integer id;
    private UserResponse userResponse;
    private PostCategoryDTO postCategory;
    private PackageDTO packageId;
    private DestinyDTO destiny;
    private String content;
    private String title;
    private Integer likeNumber;
    private Integer dislikeNumber;
    private Integer shareNumber;
    private String status;
    private Instant createdDate;
    private String createdBy;
    private Instant updatedDate;
    private String updatedBy;
    List<CommentDTO> comments;
    List<PostImageDTO> images;

}
