package com.example.demo.member;


import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("회원가입")
    void t1(){
        String username = "user10";
        String password = "1234";
        String email = "user10@test.com";
        String nickname = "user10";
        memberService.join(username, password, email,nickname);

        Member foundMember = memberService.findByUsername("user10").get();
        assertThat(foundMember.getCreateDate()).isNotNull();
        assertThat(foundMember.getUsername()).isNotNull();
        assertThat(passwordEncoder.matches(password, foundMember.getPassword())).isTrue();
    }
    @Test
    @DisplayName("회원가입_닉네임 미 기입 회원")
    void t2() {
        String username = "user11";
        String password = "1234";
        String email = "user11@test.com";
        String nickname = null;
        memberService.join(username, password, email,nickname);

        Member foundMember = memberService.findByUsername("user11").get();
        assertThat(foundMember.getCreateDate()).isNotNull();
        assertThat(foundMember.getUsername()).isNotNull();
        assertThat(foundMember.getNickname()).isNotNull();
        assertThat(passwordEncoder.matches(password, foundMember.getPassword())).isTrue();
    }



    @Test
    @DisplayName("회원가입_닉네임 미 기입 회원")
    void t3() throws MessagingException {
        String username = "user11";
        String password = "1234";
        String email = "dvum0045@gmail.com";
        String nickname = null;
        memberService.join(username, password, email,nickname);

        memberService.welcomeMail(email);
        Member foundMember = memberService.findByUsername("user11").get();
        assertThat(foundMember.getCreateDate()).isNotNull();
        assertThat(foundMember.getUsername()).isNotNull();
        assertThat(foundMember.getNickname()).isNull();
        assertThat(passwordEncoder.matches(password, foundMember.getPassword())).isTrue();
    }

}
