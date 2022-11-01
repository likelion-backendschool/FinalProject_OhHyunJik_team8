package com.example.demo.app.base.initData;


import com.example.demo.app.cart.service.CartService;
import com.example.demo.app.cash.service.CashService;
import com.example.demo.app.keyword.service.KeywordService;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.order.service.OrderService;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
public class DevInitData implements InitDataBefore {
    private boolean initDataDone = false; // 두번 실행 방지를 위해

    @Bean
    CommandLineRunner initData(
            MemberService memberService, PostService postService, ProductService productService, KeywordService keywordService, CartService cartService, CashService cashService, OrderService orderService) {
        return args -> {
            if (initDataDone) return;

            initDataDone = true;
            before(memberService,postService,productService,keywordService,cartService, cashService,orderService);
        };
    }
}

