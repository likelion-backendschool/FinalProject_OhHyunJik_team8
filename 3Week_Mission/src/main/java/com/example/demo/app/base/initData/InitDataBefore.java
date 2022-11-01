package com.example.demo.app.base.initData;



import com.example.demo.app.cart.service.CartService;
import com.example.demo.app.cash.service.CashService;
import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.keyword.service.KeywordService;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.order.entity.Order;
import com.example.demo.app.order.service.OrderService;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.product.entity.Product;
import com.example.demo.app.product.service.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService, KeywordService keywordService, CartService cartService, CashService cashService, OrderService orderService){

        class Helper {
            public Order order(Member member, List<Product> products) {
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);

                    cartService.addItem(member, product);
                }

                return orderService.createFromCart(member);
            }
        }
        Helper helper = new Helper();

        Member member1 = memberService.join("user1", "1234", "user1@test.com","");
        Member member2 = memberService.join("user2", "1234", "user2@test.com","user2");
        Member member3 = memberService.join("user3", "1234", "user3@test.com",null);

        List<String> keyword1 = new ArrayList<>();
        List<String> keyword2 = new ArrayList<>();
        keyword1.add("키워드1");
        keyword1.add("키워드2");
        keyword1.add("키워드3");
        keyword2.add("키워드2");
        keyword2.add("키워드4");
        keyword2.add("키워드5");


        Post post1 = postService.write(member1,"제목 1" ,"- 내용1",keyword1);
        Post post2 = postService.write(member1,"제목 2" ,"- 내용3",keyword2);

        Keyword productTag1 = keywordService.getKeyWordByContent("키워드1");
        Keyword productTag2 = keywordService.getKeyWordByContent("키워드4");

        Keyword productTag3 = keywordService.getKeyWordByContent("키워드2");
        Keyword productTag4 = keywordService.getKeyWordByContent("키워드1");

        Product product1 =productService.create(member2,"상품1",5000,productTag1);
        Product product2 =productService.create(member1,"상품2",10000,productTag2);

        Product product3 =productService.create(member2,"상품3",5000,productTag3);
        Product product4 =productService.create(member1,"상품4",100000,productTag4);


        memberService.addCash(member1, 10_000, "충전__무통장입금");
        memberService.addCash(member1, 20_000, "충전__무통장입금");
        memberService.addCash(member1, -5_000, "출금__일반");
        memberService.addCash(member1, 1_000_000, "충전__무통장입금");

        memberService.addCash(member2, 2_000_000, "충전__무통장입금");

        // 1번 주문 : 결제완료
        Order order1 = helper.order(member1, Arrays.asList(
                        product1,
                        product2
                )
        );

        int order1PayPrice = order1.calculatePayPrice();
        orderService.payByRestCashOnly(order1);

        // 2번 주문 : 결제 후 환불
        Order order2 = helper.order(member2, Arrays.asList(
                        product3,
                        product4
                )
        );

        orderService.payByRestCashOnly(order2);

        orderService.refund(order2);

        // 3번 주문 : 결제 전
        Order order3 = helper.order(member2, Arrays.asList(
                        product1,
                        product2
                )
        );


        cartService.addItem(member2, product1);
        cartService.addItem(member2, product2);
        cartService.addItem(member1, product4);
        cartService.addItem(member2, product3);
    }
}
