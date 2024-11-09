package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.Post;
import com.fengshuisystem.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByStatus(Status status, Pageable pageable);
    @Query(value = "SELECT c from Post c where c.status = 'ACTIVE'")
    Page<Post> findAllByPostCategory(String postCategory, Pageable pageable);
    @Query(value = "SELECT c from Post c where c.status = 'ACTIVE'")
    Page<Post> findAllByAccount_Email(String email, Pageable pageable);
    @Query(value = "SELECT c from Post c where c.status = 'ACTIVE'")
    Page<Post> findAllByTitleContaining(String title, Pageable pageable);
}
