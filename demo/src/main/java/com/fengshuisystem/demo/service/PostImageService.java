package com.fengshuisystem.demo.service;
import com.fengshuisystem.demo.dto.AnimalImageDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.PostImageDTO;

import java.util.List;

public interface PostImageService {
    public List<PostImageDTO> getAllPostImage(Integer id);

}
