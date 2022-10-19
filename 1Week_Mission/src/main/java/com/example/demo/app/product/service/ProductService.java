package com.example.demo.app.product.service;


import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.post.repository.PostRepository;
import com.example.demo.app.product.entity.Product;
import com.example.demo.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    public Product create(Member member2, String subject, int price, Keyword keyWord) {
        Product product = Product
                .builder()
                .seller(member2)
                .postKeyword(keyWord)
                .subject(subject)
                .price(price)
                .build();
        productRepository.save(product);
        return product;

    }
    @Transactional(readOnly = true)
    public Optional<Product> productById(Long id) {
        return productRepository.findById(id);


    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
