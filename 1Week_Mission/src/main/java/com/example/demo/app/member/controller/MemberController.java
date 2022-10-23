package com.example.demo.app.member.controller;


import com.example.demo.app.member.dto.PostFindUserNameReq;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String postJoin(@Valid PostLoginReq joinForm) throws MessagingException {
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
    public String editModifyProfile(@AuthenticationPrincipal MemberContext memberContext, @Valid PostProfileReq modifyFrom, HttpSession httpSession) {
        Optional<Member> member = memberService.findByUserId(memberContext.getId());
        memberService.modifyProfile(member.get(),modifyFrom.getEmail(),modifyFrom.getNickname());
        httpSession.invalidate();
        return "redirect:/member/login?msg=" + Ut.url.encode("프로필을 수정했습니다.");
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUsername")
    public String showFindUsername(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        return "member/findUsername";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String postFindUsername(Model model,@Valid PostFindUserNameReq findUserNameReq) {
        Optional<Member> member = memberService.findByEmail(findUserNameReq.getEmail());
        if(member.isEmpty()){
            return "redirect:/member/findUsername?msg=" + Ut.url.encode("해당 이메일로 가입된 계정은 없습니다.");
        }

        return "redirect:/member/findUsername?msg=" + Ut.url.encode("해당 이메일로 가입된 아이디는 "+member.get().getUsername()+" 입니다.");
    }

    @GetMapping("/idCheck")
    @ResponseBody
    public String nicknameCheck(String username){
        Optional<Member> users_ = memberService.findByUsername(username);
        if (!users_.isPresent()){
            return "사용 가능한 아이디입니다.";
        }
        return "닉네임이 중복 되었습니다.";
    }

    @GetMapping("/emailCheck")
    @ResponseBody
    public String emailCheck(String email){
        Optional<Member> users_ = memberService.findByEmail(email);
        if (!users_.isPresent()){
            return "사용 가능한 이메일입니다.";
        }
        return "이메일이 중복 되었습니다.";
    }

}
