package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
