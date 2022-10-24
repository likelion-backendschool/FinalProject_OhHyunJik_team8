package com.example.demo.app.member.service;


import com.example.demo.app.member.exception.AlreadyJoinException;
import com.example.demo.app.member.repository.MemberRepository;
import com.example.demo.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") // 회원가입 발송메일주소
    private String from;

    public Member join(String username, String password, String email, String nickname){
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new AlreadyJoinException();
        } // 해당건은 에러 처리를 어떻게 던져줄것인지 고민해보고 정하기로
            Member member = Member.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .nickname(nickname)
                    .build();
            memberRepository.save(member);
        return member;
    }

    public void welcomeMail(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        mimeMessageHelper.setFrom(from); // 보낼 주소
        mimeMessageHelper.setTo(email); // 받을 주소
        mimeMessageHelper.setSubject("안녕하세요 백엔드스쿨"); // 제목

        StringBuilder body = new StringBuilder();

        body.append("회원가입을 환영합니다"); // 내용
        mimeMessageHelper.setText(body.toString(), true);
        javaMailSender.send(mimeMessage);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public void modifyProfile(Member member,String email, String nickname) {
        member.setEmail(email);
        member.setNickname(nickname);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUserId(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member;
    }

}
