package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    
}
