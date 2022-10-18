package com.example.demo.app.post.service;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    public Post write(Long authorId, String subject, String content) {
        return write(new Member(authorId), subject, content);
    }
    public Post write(Long authorId, String subject, String content, List<String> hashTags) {
        return write(new Member(authorId), subject, content, hashTags);
    }

    public Post write(Member author, String subject, String content) {
        return write(author, subject, content, null);
    }
    public Post write(Member author, String subject, String content,List<String> hashTags) {
        Post post = Post
                .builder()
                .author(author)
                .subject(subject)
                .content(content)
                .build();

        postRepository.save(post);
        createHashTag(post,hashTags);
        return post;
    }

    private void createHashTag(Post post, List<String> tags) {
        for (String tag : tags) {
            post.addPostHashTags(tag);
        }
        postRepository.save(post);
    }

    public Post getPostFromId(Long id) {
        Post post = getArticleById(id);
        return post;
    }

    private Post getArticleById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}
