package com.example.demo.app.hashtag.service;

import com.example.demo.app.hashtag.entity.HashTag;
import com.example.demo.app.hashtag.repository.HashTagRepository;
import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.keyword.service.KeywordService;
import com.example.demo.app.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashTagService {
    private final KeywordService keywordService;
    private final HashTagRepository hashTagRepository;

    @Transactional
    public void applyHashTags(Post post, List<String> keywordContents) {
        List<HashTag> oldHashTags = getHashTags(post);
        List<HashTag> needToDelete = new ArrayList<>();
        for (HashTag oldHashTag : oldHashTags) {
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHashTag.getKeyword().getContent()));
            if (contains == false) {
                needToDelete.add(oldHashTag);
            }
        }
        needToDelete.forEach(hashTag -> {
            hashTagRepository.delete(hashTag);
        });
        keywordContents.forEach(keywordContent -> {
            saveHashTag(post, keywordContent);
        });
    }

    private HashTag saveHashTag(Post post, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);
        Optional<HashTag> opHashTag = hashTagRepository.findByPostIdAndKeywordId(post.getId(), keyword.getId());
        if (opHashTag.isPresent()) {
            return opHashTag.get();
        }
        HashTag hashTag = HashTag.builder()
                .post(post)
                .keyword(keyword)
                .build();
        hashTagRepository.save(hashTag);
        return hashTag;
    }

    public List<HashTag> getHashTags(Post post) {
        return hashTagRepository.findAllByPostId(post.getId());
    }

    public List<HashTag> getHashTagsByPostIdIn(long[] ids) {
        return hashTagRepository.findAllByPostIdIn(ids);
    }

    @Transactional
    public void delete(Long id) {
        hashTagRepository.deleteAllByPostId(id);
    }
}
