package com.example.demo.post;


import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("글 생성")
    void t1(){
        Member writer = memberService.findByUsername("user1").get();
        String subject = "subject";
        String content = "- content" +
                "<h1>content1</h1>";


        Post post = postService.write(writer, subject, content);
        assertThat(post.getAuthor()).isNotNull();
        assertThat(post.getCreateDate()).isNotNull();
        assertThat(post.getContent()).isNotNull();

    }
}
