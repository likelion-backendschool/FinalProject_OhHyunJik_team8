package com.example.demo.app.product.controller;

import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.keyword.service.KeywordService;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.product.dto.PostProductReq;
import com.example.demo.app.product.entity.Product;
import com.example.demo.app.product.service.ProductService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final KeywordService keywordService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String showList(Model model) {
        List<Product> products = productService.getProducts();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable Long id) {
        Optional<Product> product = productService.productById(id);
        if(product.isEmpty()){
            return "redirect:/product/list?msg=" + Ut.url.encode("잘못된 상품 접근");
        }
        model.addAttribute("product",product.get());
        return "product/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String getWrite(Model model) {
        List<Keyword> keywords = keywordService.findAll();
        model.addAttribute("keywords",keywords);
        return "product/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String PostWrite(@AuthenticationPrincipal MemberContext memberContext, @Valid PostProductReq postProductReq) {
        Keyword keyword = keywordService.getKeyWordByContent(postProductReq.getKeyword());
        Optional<Member> member = memberService.findByUserId(memberContext.getId());
        Product product = productService.create(member.get(),postProductReq.getSubject(),postProductReq.getPrice(),keyword);
        String msg = "%d번 게시물이 작성되었습니다.".formatted(product.getId());
        msg = Ut.url.encode(msg);
        return "redirect:/product/%d?msg=%s".formatted(product.getId(), msg);
    }

}
