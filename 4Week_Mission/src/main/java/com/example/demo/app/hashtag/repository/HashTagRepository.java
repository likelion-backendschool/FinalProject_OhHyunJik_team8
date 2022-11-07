package com.example.demo.app.hashtag.repository;


import com.example.demo.app.hashtag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByPostIdAndKeywordId(Long articleId, Long keywordId);

    List<HashTag> findAllByPostId(Long articleId);

    List<HashTag> findAllByPostIdIn(long[] ids);

    void deleteAllByPostId(Long id);
}