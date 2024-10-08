package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

}
