package com.example.demo.app.cart.controller;

import com.example.demo.app.cart.entity.CartItem;
import com.example.demo.app.cart.service.CartService;
import com.example.demo.app.member.entity.Member;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/lists")
    @PreAuthorize("isAuthenticated()")
    public String showItems(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member buyer = memberContext.getMember();
        List<CartItem> items = cartService.getItemsByBuyer(buyer);
        model.addAttribute("items", items);
        return "cart/lists";
    }

    @PostMapping("/removeItems")
    @PreAuthorize("isAuthenticated()")
    public String removeItems(@AuthenticationPrincipal MemberContext memberContext, String ids) {
        Member buyer = memberContext.getMember();
        String[] idsArr = ids.split(",");
        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    CartItem cartItem = cartService.findItemById(id).orElse(null);

                    if (cartService.actorCanDelete(buyer, cartItem)) {
                        cartService.removeItem(cartItem);
                    }
                });
        return "redirect:/cart/lists?msg=" + Ut.url.encode("%d건의 품목을 삭제하였습니다.".formatted(idsArr.length));
    }

    @GetMapping("/add/{id}")
    @PreAuthorize("isAuthenticated()")
    public String addItems(@AuthenticationPrincipal MemberContext memberContext,@PathVariable Long id) {
        Member buyer = memberContext.getMember();
        Optional<Product> product = productService.productById(id);
        cartService.addItem(buyer,product.get());
        return "redirect:/cart/lists?msg=" + Ut.url.encode("%d번상품을 장바구니에 추가하였습니다.".formatted(id));
    }

}
