package com.example.demo.app.withdraw.service;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.withdraw.dto.PostWithDrawReq;
import com.example.demo.app.withdraw.entity.WithDraw;
import com.example.demo.app.withdraw.repository.WithDrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class WithDrawService {
    private final WithDrawRepository withDrawRepository;

    @Transactional
    public WithDraw create(PostWithDrawReq postWithDrawReq, Member member) {
        WithDraw withDraw = WithDraw
                .builder()
                .member(member)
                .accountName(postWithDrawReq.getAccountName())
                .accountNumber(postWithDrawReq.getAccountNumber())
                .requestCash(postWithDrawReq.getRequestCash())
                .status("신청중")
                .build();
        withDrawRepository.save(withDraw);
        return withDraw;
    }
}
