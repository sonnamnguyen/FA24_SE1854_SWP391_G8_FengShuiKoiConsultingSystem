package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.entity.PostCategory;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.repository.PostCategoryRepository;
import com.fengshuisystem.demo.service.PostCategoryService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class PostCategoryServiceImpl implements PostCategoryService {
    private final PostCategoryRepository postCategoryRepository;

    public PostCategoryServiceImpl(PostCategoryRepository postCategoryRepository) {
        this.postCategoryRepository = postCategoryRepository;
    }

    @Override
    public List<PostCategory> getAllPostCategories() {
        return postCategoryRepository.findAll();
    }

    @Override
    public PostCategory getPostCategoryById(Integer id) {
        return postCategoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_EXISTED));
    }

    @Override
    public PostCategory createPostCategory(PostCategory postCategory) {
        return postCategoryRepository.save(postCategory);
    }

    @Override
    public PostCategory updatePostCategory(Integer id, PostCategory postCategory) {
        Optional<PostCategory> existingPostCategory = postCategoryRepository.findById(id);
        if (existingPostCategory.isPresent()) {
            PostCategory updatedCategory = existingPostCategory.get();
            updatedCategory.setPostCategoryName(postCategory.getPostCategoryName());
            return postCategoryRepository.save(updatedCategory);
        } else {
            // Xử lý trường hợp không tìm thấy PostCategory (ví dụ: ném ngoại lệ)
            throw new AppException(ErrorCode.POST_CATEGORY_NOT_EXISTED);
        }
    }


    @Override
    public void deletePostCategory(Integer id) {
        Optional<PostCategory> existingPostCategory = postCategoryRepository.findById(id);
        if (existingPostCategory.isPresent()) {
            postCategoryRepository.deleteById(id);
        } else {
            throw new AppException(ErrorCode.POST_CATEGORY_NOT_EXISTED);
        }
    }
}
