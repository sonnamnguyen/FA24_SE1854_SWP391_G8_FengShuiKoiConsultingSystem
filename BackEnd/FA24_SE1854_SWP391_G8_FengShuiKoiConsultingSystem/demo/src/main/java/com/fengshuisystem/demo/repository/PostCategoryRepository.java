package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Integer> {
     boolean existsByPostCategoryName(String postCategoryName);

}
