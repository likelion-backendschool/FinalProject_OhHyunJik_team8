package com.example.demo.app.post.controller;

import com.example.demo.app.hashtag.entity.HashTag;
import com.example.demo.app.hashtag.service.HashTagService;
import com.example.demo.app.member.dto.PostLoginReq;
import com.example.demo.app.post.dto.PostWriteReq;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final HashTagService hashTagService;

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(value = "kw", defaultValue = "") String kw
            , @RequestParam(defaultValue = "keyword") String kwType) {
        List<Post> posts = postService.getPosts(kwType,kw);
        postService.loadForPrintData(posts);
        model.addAttribute("posts", posts);
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String getWrite() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String postWrite(@AuthenticationPrincipal MemberContext memberContext, @Valid PostWriteReq postWriteReq, @RequestParam(value = "tag", required = false) List<String> tags) {
        Post post = postService.write(memberContext.getId(), postWriteReq.getSubject(), postWriteReq.getContent(), tags);
        String msg = "%d번 게시물이 작성되었습니다.".formatted(post.getId());
        msg = Ut.url.encode(msg);
        return "redirect:/post/%d?msg=%s".formatted(post.getId(), msg);
    }

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable Long id) {
        Post post = postService.getForPrintPostById(id);
        model.addAttribute("post", post);
        return "post/detail";
    }

    @GetMapping("/{id}/modify")
    public String showModify(Model model, @PathVariable Long id) {
        Post post = postService.getForPrintPostById(id);
        model.addAttribute("post", post);
        return "post/modify";
    }


    @GetMapping("/{id}/delete")
    public String deleteDetail(Model model, @PathVariable Long id) {
        postService.delete(id);
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id,
                         @Valid PostWriteReq articleForm) {
        Post post = postService.getForPrintPostById(id);
        if (memberContext.memberIsNot(post.getAuthor())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        postService.modify(post, articleForm.getSubject(), articleForm.getContent());
        String msg = Ut.url.encode("%d번 게시물이 수정되었습니다.".formatted(id));
        return "redirect:/post/%d?msg=%s".formatted(id, msg);
    }
}
