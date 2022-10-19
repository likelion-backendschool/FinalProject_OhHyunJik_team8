package com.example.demo.app.keyword.service;

import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public Keyword save(String keywordContent) {
        Optional<Keyword> optKeyword = keywordRepository.findByContent(keywordContent);

        if ( optKeyword.isPresent() ) {
            return optKeyword.get();
        }

        Keyword keyword = Keyword
                .builder()
                .content(keywordContent)
                .build();

        keywordRepository.save(keyword);

        return keyword;
    }

    public Keyword getKeyWordByContent(String keyword) {
        Optional<Keyword> productKeyword = keywordRepository.findByContent(keyword);
        return productKeyword.get();
    }

    public List<Keyword> findAll() {
        return keywordRepository.findAll();
    }
}