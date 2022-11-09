package com.example.demo.app.mybook.repository;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.mybook.dto.MyBookListRes;
import com.example.demo.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    List<MyBook> findAllByMemberId(Long id);

}
