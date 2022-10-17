package com.example.demo.app.base.initData;



import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;

public interface InitDataBefore {
    default void before(MemberService memberService){


        Member member1 = memberService.join("user1", "1234", "user1@test.com","");
        Member member2 = memberService.join("user2", "1234", "user2@test.com","user2");
        Member member3 = memberService.join("user3", "1234", "user3@test.com",null);


    }
}
