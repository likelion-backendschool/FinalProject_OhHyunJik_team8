package com.example.demo.app.member.controller;


import com.example.demo.app.member.dto.PostLoginReq;
import com.example.demo.app.member.dto.PostProfileReq;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;


    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid PostLoginReq joinForm) throws MessagingException {
        memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail(), joinForm.getNickname());
        memberService.welcomeMail(joinForm.getEmail());
        return "redirect:/member/login?msg=" + Ut.url.encode("회원가입이 완료되었습니다.");
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin(HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/member/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }

        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        model.addAttribute("model",memberContext);
        return "member/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String showModifyProfile(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        model.addAttribute("member",memberContext);
        return "member/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String editModifyProfile(@AuthenticationPrincipal MemberContext memberContext, Model model,@Valid PostProfileReq modifyFrom) {
        Optional<Member> member = memberService.findByUserId(memberContext.getId());
        memberService.modifyProfile(member.get(),modifyFrom.getEmail(),modifyFrom.getNickname());
        memberContext.setEmail(modifyFrom.getEmail());
        memberContext.setNickname(modifyFrom.getNickname());
        return "redirect:/member/profile?msg=" + Ut.url.encode("회원가입이 완료되었습니다.");
    }

}
