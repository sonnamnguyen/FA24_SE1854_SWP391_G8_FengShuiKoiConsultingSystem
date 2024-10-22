package com.fengshuisystem.demo.service;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.PostImageDTO;
public interface PostImageService {
    public PostImageDTO createPostImage(PostImageDTO postImageDTO);
    public void deletePostImage(Integer id);
    public PostImageDTO updatePostImage(Integer id, PostImageDTO postImageDTO);
    public PageResponse<PostImageDTO> getPostImages(int page, int size);

}
