package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.Post;
import com.fengshuisystem.demo.entity.enums.Status;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByStatus(Status status, Pageable pageable);
    @Query(value = "SELECT c from Post c where c.status = 'ACTIVE'")
    Page<Post> findAllByPostCategory(String postCategory, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.status = 'ACTIVE' AND p.createdBy = :email")
    Page<Post> findAllByAccount_Email(@Param("email") String email, Pageable pageable);
    @Query("SELECT c FROM Post c WHERE c.status = 'ACTIVE' AND c.title LIKE %:title%")
    Page<Post> findAllByTitleContaining(@Param("title") String title, Pageable pageable);
    @Query("SELECT COUNT(p) FROM Post p")
    long countAllPosts();
    @Query(value = "SELECT c from Post c  JOIN c.destiny d where d.destiny =:destiny and c.status = 'ACTIVE'")
    Page<Post> findAllByDestiny(Pageable pageable, String destiny);
}
