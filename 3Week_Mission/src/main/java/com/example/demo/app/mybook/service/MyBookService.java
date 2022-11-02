package com.example.demo.app.mybook.service;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.mybook.entity.MyBook;
import com.example.demo.app.mybook.repository.MyBookRepository;
import com.example.demo.app.order.entity.OrderItem;
import com.example.demo.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;

    @Transactional
    public void createMyBook(Member member, Product product){
        MyBook myBook = MyBook.builder()
                .book(product)
                .member(member)
                .build();
        myBookRepository.save(myBook);
    }

    @Transactional
    public void createMyBookList(Member member, List<OrderItem> orderItems){
        for(OrderItem orderItem: orderItems){
            createMyBook(member,orderItem.getProduct());
        }
    }

}
