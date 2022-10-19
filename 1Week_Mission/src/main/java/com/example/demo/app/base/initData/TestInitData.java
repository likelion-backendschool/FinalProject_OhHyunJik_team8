package com.example.demo.app.base.initData;

import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestInitData implements InitDataBefore {
    @Bean
    CommandLineRunner initData(
            MemberService memberService, PostService postService, ProductService productService) {
        return args -> {
            before(memberService,postService,productService);
        };
    }
}
