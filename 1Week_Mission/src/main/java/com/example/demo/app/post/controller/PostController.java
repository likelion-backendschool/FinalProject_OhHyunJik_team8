package com.example.demo.app.post.controller;

import com.example.demo.app.member.dto.PostLoginReq;
import com.example.demo.app.post.dto.PostWriteReq;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String getWrite() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String postWrite(@AuthenticationPrincipal MemberContext memberContext, @Valid PostWriteReq postWriteReq, @RequestParam(value = "tag", required = false) List<String> tags) {
        Post post = postService.write(memberContext.getId(),postWriteReq.getSubject(),postWriteReq.getContent(),tags) ;
        String msg = "%d번 게시물이 작성되었습니다.".formatted(post.getId());
        msg = Ut.url.encode(msg);
        return "redirect:/post/%d?msg=%s".formatted(post.getId(), msg);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String getPostDetail() {
        return "post/write";
    }
}
