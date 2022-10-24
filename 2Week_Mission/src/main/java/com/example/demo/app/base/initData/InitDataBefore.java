package com.example.demo.app.base.initData;



import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.keyword.service.KeywordService;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.product.entity.Product;
import com.example.demo.app.product.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService, KeywordService keywordService){
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
        Keyword productTag1 = keywordService.getKeyWordByContent("키워드1");
        Keyword productTag2 = keywordService.getKeyWordByContent("키워드4");
        Product product1 =productService.create(member2,"상품1",5000,productTag1);
        Product product2 =productService.create(member1,"상품2",10000,productTag2);
    }
}
