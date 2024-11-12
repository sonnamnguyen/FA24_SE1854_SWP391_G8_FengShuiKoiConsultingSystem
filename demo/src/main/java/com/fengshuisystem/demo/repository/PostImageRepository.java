package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    @Query( value = "SELECT a FROM PostImage a WHERE a.post.id = :id")
    List<PostImage> findByPostId(Integer id);
}
