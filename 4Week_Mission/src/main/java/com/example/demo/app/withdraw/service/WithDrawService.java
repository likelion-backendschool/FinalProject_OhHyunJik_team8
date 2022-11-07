package com.example.demo.app.withdraw.service;

import com.example.demo.app.base.dto.RsData;
import com.example.demo.app.cash.entity.CashLog;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.rebate.entity.RebateOrderItem;
import com.example.demo.app.withdraw.dto.PostWithDrawReq;
import com.example.demo.app.withdraw.entity.WithDraw;
import com.example.demo.app.withdraw.repository.WithDrawRepository;
import com.example.demo.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .build();
        withDrawRepository.save(withDraw);
        return withDraw;
    }

    public List<WithDraw> findall() {
        return withDrawRepository.findAll();
    }


    @Transactional
    public RsData complete(long id) {
        WithDraw withDraw = withDrawRepository.findById(id).get();

        if (withDraw.isComplete() == true) {
            return RsData.of("F-1", "정산을 할 수 없는 상태입니다.");
        }
        withDraw.setComplete(true);
        withDrawRepository.save(withDraw);
        return RsData.of(
                "S-1",
                "%d 번 신청에 대한 처리를 완료 했습니다.".formatted(withDraw.getId())
        );
    }
}
