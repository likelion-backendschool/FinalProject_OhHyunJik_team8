package com.example.demo.app.mybook.controller;

import com.example.demo.app.base.dto.RsData;
import com.example.demo.app.mybook.dto.MyBookListRes;
import com.example.demo.app.mybook.dto.MyBookDetail;
import com.example.demo.app.mybook.service.MyBookService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/myBooks")
public class MyBookController {
    private final MyBookService myBookService;


    @GetMapping("")
    public ResponseEntity<RsData> list(@AuthenticationPrincipal MemberContext memberContext) {
        List<MyBookListRes> myBooks = myBookService.findAll(memberContext);

        return Ut.spring.responseEntityOf(
                RsData.successOf(
                        Ut.mapOf(
                                "myBooks", myBooks
                        )
                )
        );
    }

    @GetMapping("/{myBookId}")
    public ResponseEntity<RsData> detail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long myBookId) {
        MyBookDetail myBookDetailRes = myBookService.findById(memberContext,myBookId);
        return Ut.spring.responseEntityOf(
                RsData.successOf(
                        Ut.mapOf(
                                "myBook", myBookDetailRes
                        )
                )
        );
    }
}
