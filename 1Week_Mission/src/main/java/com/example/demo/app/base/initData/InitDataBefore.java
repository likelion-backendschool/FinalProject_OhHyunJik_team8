package com.example.demo.app.base.initData;



import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.service.PostService;

import java.util.ArrayList;
import java.util.List;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService){
        List<String> keyword1 = new ArrayList<>();
        List<String> keyword2 = new ArrayList<>();
        keyword1.add("키워드1");
        keyword1.add("키워드2");
        keyword1.add("키워드3");
        keyword2.add("키워드2");
        keyword2.add("키워드4");
        keyword2.add("키워드5");
        Member member1 = memberService.join("user1", "1234", "user1@test.com","");
        Member member2 = memberService.join("user2", "1234", "user2@test.com","user2");
        Member member3 = memberService.join("user3", "1234", "user3@test.com",null);
        Post post1 = postService.write(member1,"제목 1" ,"- 내용1",keyword1);
        Post post2 = postService.write(member1,"제목 2" ,"- 내용3",keyword2);

    }
}
