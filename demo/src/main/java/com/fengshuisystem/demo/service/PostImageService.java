package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PostImageDTO;

import java.util.List;

public interface PostImageService {
    List<PostImageDTO> getAllPostImage(Integer id);

}
