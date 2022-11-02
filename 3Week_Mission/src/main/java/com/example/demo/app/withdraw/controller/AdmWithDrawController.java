package com.example.demo.app.withdraw.controller;


import com.example.demo.app.base.dto.RsData;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.security.dto.MemberContext;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/adm/withdraw")
@RequiredArgsConstructor
@Slf4j
public class AdmWithDrawController {
    private final WithDrawService withDrawService;
    private final MemberService memberService;

    @GetMapping("/applyList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admShowApplyList(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        List<WithDraw> withDrawList = withDrawService.findall();
        model.addAttribute("withDrawList",withDrawList);
        return "withdraw/adm/applyList";
    }

    @PostMapping("/requestOne/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable long id, HttpServletRequest req) {
        RsData rebateRsData = withDrawService.complete(id);

        String referer = req.getHeader("Referer");
        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/withdraw/applyList";

        redirect = rebateRsData.addMsgToUrl(redirect);

        return redirect;
    }
}
