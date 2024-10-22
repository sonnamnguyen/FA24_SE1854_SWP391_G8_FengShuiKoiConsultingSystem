package com.fengshuisystem.demo.dto;
import com.fengshuisystem.demo.entity.Post;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostImageDTO {
    private Integer id;
    private PostDTO post;
    private String imageName;
    private String imageUrl;
    private String status;
    private Instant createdDate;
    private String createdBy;
    private Instant updatedDate;
    private String updatedBy;
}
