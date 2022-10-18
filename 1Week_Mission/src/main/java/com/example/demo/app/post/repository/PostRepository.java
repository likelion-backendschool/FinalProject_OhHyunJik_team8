package com.example.demo.app.post.repository;

import com.example.demo.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PostRepository extends JpaRepository<Post,Long> {

}