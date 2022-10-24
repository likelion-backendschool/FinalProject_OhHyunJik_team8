package com.example.demo.app.product.repository;


import com.example.demo.app.post.entity.Post;
import com.example.demo.app.product.entity.Product;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;


public interface ProductRepository extends JpaRepository<Product,Long> {
}
