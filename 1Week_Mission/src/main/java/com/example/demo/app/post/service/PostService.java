package com.example.demo.app.post.service;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    public Post write(Long authorId, String subject, String content) {
        return write(new Member(authorId), subject, content);
    }
    public Post write(Member author, String subject, String content) {
        return write(author, subject, content, "");
    }
    public Post write(Member author, String subject, String content, String hashTagsStr) {
        Post post = Post
                .builder()
                .author(author)
                .subject(subject)
                .content(content)
                .build();

        postRepository.save(post);

        return post;
    }
}
