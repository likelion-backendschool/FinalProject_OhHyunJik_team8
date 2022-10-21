package com.example.demo.app.post.repository;

import com.example.demo.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post,Long> {

    @Query(nativeQuery = true,value = "SELECT DISTINCT A.*\n" +
            "FROM keyword AS K\n" +
            "INNER JOIN hash_tag AS HT\n" +
            "ON K.id = HT.keyword_id\n" +
            "INNER JOIN post AS A\n" +
            "ON A.id = HT.post_id\n" +
            "WHERE K.content =:content")
    List<Post> findByHashTagContains(@Param("content")String kw);
}
