package com.example.demo.app.member.service;


import com.example.demo.app.base.dto.RsData;
import com.example.demo.app.cash.entity.CashLog;
import com.example.demo.app.cash.repository.CashLogRepository;
import com.example.demo.app.cash.service.CashService;
import com.example.demo.app.member.exception.AlreadyJoinException;
import com.example.demo.app.member.repository.MemberRepository;
import com.example.demo.app.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final CashService cashService;

    @Value("${spring.mail.username}") // 회원가입 발송메일주소
    private String from;

    @Transactional
    public Member join(String username, String password, String email, String nickname){
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new AlreadyJoinException();
        }

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

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public void modifyProfile(Member member,String email, String nickname) {
        member.updateInfo(email,nickname);
        memberRepository.save(member);
    }

    public Optional<Member> findByUserId(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member;
    }

    @Transactional
    public RsData<AddCashRsDataBody> addCash(Member member, long price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);
        Optional<Member> updateMember = findByUserId(member.getId());
        long newRestCash = updateMember.get().getRestCash() + cashLog.getPrice();
        member.updateRestCash(newRestCash);
        memberRepository.save(member);
        return RsData.of(
                "S-1",
                "성공",
                new AddCashRsDataBody(cashLog, newRestCash)
        );
    }

    @Data
    @AllArgsConstructor
    public static class AddCashRsDataBody {
        CashLog cashLog;
        long newRestCash;
    }

    public long getRestCash(Member member) {
        Member foundMember = findByUsername(member.getUsername()).get();
        return foundMember.getRestCash();
    }
}
