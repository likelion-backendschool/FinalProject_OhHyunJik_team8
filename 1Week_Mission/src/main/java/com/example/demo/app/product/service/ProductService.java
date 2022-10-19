package com.example.demo.app.product.service;


import com.example.demo.app.member.entity.Member;
import com.example.demo.app.post.repository.PostRepository;
import com.example.demo.app.product.entity.Product;
import com.example.demo.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    public Product create(Member member2, String 상품1, int price, String 키워드1) {


    }
}
