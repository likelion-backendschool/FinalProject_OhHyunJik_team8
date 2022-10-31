package com.example.demo.app.product.service;


import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.post.entity.Post;
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
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(Member seller, String subject, int price, Keyword keyword) {
        Product product = Product
                .builder()
                .seller(seller)
                .postKeyword(keyword)
                .subject(subject)
                .price(price)
                .build();
        productRepository.save(product);
        return product;
    }

    public Optional<Product> productById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
