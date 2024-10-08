package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;



public interface PostRepository extends JpaRepository<Post, Integer> {

}
