package com.example.demo.app.mybook.service.repository;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
}
