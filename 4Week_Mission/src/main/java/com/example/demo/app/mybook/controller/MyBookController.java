package com.example.demo.app.mybook.controller;

import com.example.demo.app.base.dto.RsData;
import com.example.demo.app.mybook.dto.MyBookListRes;
import com.example.demo.app.mybook.dto.MyBookDetail;
import com.example.demo.app.mybook.service.MyBookService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.ALL_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/myBooks")
@RequiredArgsConstructor
@Tag(name = "ApiV1MyBooksController", description = "로그인 된 회윈이 구매한 책 정보")
public class MyBookController {
    private final MyBookService myBookService;


    @GetMapping(value = "")
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
