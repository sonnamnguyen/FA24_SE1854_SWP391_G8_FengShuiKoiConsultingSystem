package com.fengshuisystem.demo.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.Package;
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
    private Account account;
    private PostCategory postCategory;
    private Package packageField;
    private Destiny destiny;
    private String content;
    private Integer likeNumber;
    private Integer dislikeNumber;
    private Integer shareNumber;
    private String status;
    private Instant createdDate;
    private String createdBy;
    private Instant updatedDate;
    private String updatedBy;
    List<CommentDTO> comments;
    List<PostImageDTO> postImages;
}
