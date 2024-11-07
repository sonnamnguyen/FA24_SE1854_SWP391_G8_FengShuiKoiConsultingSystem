package com.fengshuisystem.demo.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
//    @JsonIgnore
//    private Integer postId;
//    private PostDTO post;
    private String imageUrl;

}
