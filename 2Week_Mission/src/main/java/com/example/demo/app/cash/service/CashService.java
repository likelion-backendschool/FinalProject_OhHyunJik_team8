package com.example.demo.app.cash.service;

import com.example.demo.app.cash.entity.CashLog;
import com.example.demo.app.cash.repository.CashLogRepository;
import com.example.demo.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashService {
    private final CashLogRepository cashLogRepository;

    @Transactional
    public CashLog addCash(Member member, long price, String eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .eventType(eventType)
                .build();
        cashLogRepository.save(cashLog);

        return cashLog;
    }
}
