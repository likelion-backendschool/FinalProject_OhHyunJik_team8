package com.example.demo.app.base.initData;


import com.example.demo.app.keyword.service.KeywordService;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevInitData implements InitDataBefore {
    @Bean
    CommandLineRunner initData(
            MemberService memberService, PostService postService, ProductService productService, KeywordService keywordService) {
        return args -> {
            before(memberService,postService,productService,keywordService);
        };
    }
}

