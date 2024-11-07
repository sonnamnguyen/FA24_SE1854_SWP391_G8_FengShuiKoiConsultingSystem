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
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTO {
    private Integer id;
    private Integer postId;
    private String content;
    private String status;
    private Instant createdDate = Instant.now();
    private String createdBy;
    private Instant updatedDate = Instant.now();
    private String updatedBy;
}
