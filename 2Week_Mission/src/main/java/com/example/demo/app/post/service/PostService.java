package com.example.demo.app.post.service;

import com.example.demo.app.hashtag.entity.HashTag;
import com.example.demo.app.hashtag.service.HashTagService;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final HashTagService hashTagService;

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
        hashTagService.applyHashTags(post,hashTags);
        return post;
    }

    private Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        hashTagService.delete(id);
        postRepository.deleteById(id);
    }

    public Post getForPrintPostById(Long id) {
        Post post = getPostById(id);
        loadForPrintData(post);
        return post;
    }

    private void loadForPrintData(Post post) {
        List<HashTag> hashTags = hashTagService.getHashTags(post);
        post.getExtra().put("hashTags", hashTags);
    }

    public List<Post> getPosts(String keyType, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        if (kw == null || kw.trim().length() == 0) {
            return postRepository.findAll();
        }
        if (keyType.equals("keyword")) {
            return  postRepository.findByHashTagContains(kw);
        }
        return postRepository.findAll();
    }

    public void loadForPrintData(List<Post> posts) {
        long[] ids = posts
                .stream()
                .mapToLong(Post::getId)
                .toArray();
        List<HashTag> hashTagsByPostIds = hashTagService.getHashTagsByPostIdIn(ids);
        Map<Long, List<HashTag>> hashTagsByArticleIdsMap = hashTagsByPostIds.stream()
                .collect(groupingBy(
                        hashTag -> hashTag.getPost().getId(), toList()
                ));
        posts.stream().forEach(article -> {
            List<HashTag> hashTags = hashTagsByArticleIdsMap.get(article.getId());
            if (hashTags == null || hashTags.size() == 0) return;
            article.getExtra().put("hashTags", hashTags);
        });
    }

    public void modify(Post post, String subject, String content) {
        post.setSubject(subject);
        post.setContent(content);
        postRepository.save(post);
    }

}
