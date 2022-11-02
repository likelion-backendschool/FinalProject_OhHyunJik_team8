package com.example.demo.app.withdraw.controller;

import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.product.dto.PostProductReq;
import com.example.demo.app.product.entity.Product;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.app.withdraw.dto.PostWithDrawReq;
import com.example.demo.app.withdraw.entity.WithDraw;
import com.example.demo.app.withdraw.service.WithDrawService;
import com.example.demo.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/withdraw")
@RequiredArgsConstructor
@Slf4j
public class WithDrawController {
    private final WithDrawService withDrawService;
    private final MemberService memberService;

    @GetMapping("/apply")
    @PreAuthorize("isAuthenticated()")
    public String showApply(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();

        long restCash = memberService.getRestCash(member);
        model.addAttribute("actorRestCash", restCash);
        return "withdraw/apply";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/apply")
    public String postApply(@AuthenticationPrincipal MemberContext memberContext, @Valid PostWithDrawReq postWithDrawReq) {
        Member member = memberContext.getMember();

        WithDraw withDraw = withDrawService.create(postWithDrawReq,member);
        String msg = "%d번 출금신청서 작성하였습니다.".formatted(withDraw.getId());
        msg = Ut.url.encode(msg);
        return "redirect:/?msg=%s".formatted(withDraw.getId(), msg);
    }

}
