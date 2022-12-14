package com.example.demo.app.order.controller;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.order.entity.Order;
import com.example.demo.app.order.exception.ActorCanNotPayOrderException;
import com.example.demo.app.order.exception.ActorCanNotSeeOrderException;
import com.example.demo.app.order.exception.OrderIdNotMatchedException;
import com.example.demo.app.order.exception.OrderNotEnoughRestCashException;
import com.example.demo.app.order.service.OrderService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final MemberService memberService;

    @Value("${secret.key}")
    private String SECRET_KEY;

    @PostMapping("/{id}/payByRestCashOnly")
    @PreAuthorize("isAuthenticated()")
    public String payByRestCashOnly(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id) {
        Order order = orderService.findForPrintById(id).get();

        Member actor = memberContext.getMember();

        long restCash = memberService.getRestCash(actor);

        if (orderService.actorCanPayment(actor, order) == false) {
            throw new ActorCanNotPayOrderException();
        }

        orderService.payByRestCashOnly(order);

        return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("??????????????? ??????????????????."));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Order order = orderService.findForPrintById(id).get();

        Member actor = memberContext.getMember();

        long restCash = memberService.getRestCash(actor);

        if (orderService.actorCanSee(actor, order) == false) {
            throw new ActorCanNotSeeOrderException();
        }

        model.addAttribute("order", order);
        model.addAttribute("actorRestCash", restCash);

        return "order/detail";
    }

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }



    @RequestMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable long id,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model,
            @AuthenticationPrincipal MemberContext memberContext
    ) throws Exception {

        Order order = orderService.findForPrintById(id).get();

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if (id != orderIdInputed) {
            throw new OrderIdNotMatchedException();
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 ?????? ???????????? ??????
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        Member actor = memberContext.getMember();
        long restCash = memberService.getRestCash(actor);
        long payPriceRestCash = order.calculatePayPrice() - amount;

        if (payPriceRestCash > restCash) {
            throw new OrderNotEnoughRestCashException();
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order, payPriceRestCash);

            return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("????????? ?????????????????????."));
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }

    @PostMapping("/makeOrder")
    @PreAuthorize("isAuthenticated()")
    public String makeOrder(@AuthenticationPrincipal MemberContext memberContext) {
        Member member = memberContext.getMember();
        Order order = orderService.createFromCart(member);
        String redirect = "redirect:/order/%d".formatted(order.getId()) + "?msg=" + Ut.url.encode("%d??? ????????? ?????????????????????.".formatted(order.getId()));

        return redirect;
    }

    @GetMapping("/lists")
    @PreAuthorize("isAuthenticated()")
    public String showLists(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member buyer = memberContext.getMember();
        List<Order> orderList = orderService.getOrders(buyer);
        model.addAttribute("orderList", orderList);
        return "order/lists";
    }

    @GetMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public String deleteOrder(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id) {
        orderService.delete(id);
        return  "redirect:/order/lists" + "?msg=" + Ut.url.encode("??? ?????? ????????? ?????????????????????.");
    }

    @GetMapping("/{id}/refund")
    @PreAuthorize("isAuthenticated()")
    public String refundOrder(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id) {
        Optional<Order> order = orderService.findForPrintById(id);
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(order.get().getModifyDate(), now);
        long duration_s = duration.getSeconds();

        if(duration_s>=60){
            return  "redirect:/order/lists" + "?msg=" + Ut.url.encode("?????? ?????? ????????? ??????????????????.");
        }

        orderService.refund(order.get());
        return  "redirect:/order/lists" + "?msg=" + Ut.url.encode("???????????? ?????? ????????????????????");
    }
}
