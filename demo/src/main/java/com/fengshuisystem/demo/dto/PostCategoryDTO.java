package com.fengshuisystem.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengshuisystem.demo.entity.Post;
import com.fengshuisystem.demo.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCategoryDTO {
    private String status;
    private Integer id;
    private String postCategoryName;
    private List<Post> posts ;
}
