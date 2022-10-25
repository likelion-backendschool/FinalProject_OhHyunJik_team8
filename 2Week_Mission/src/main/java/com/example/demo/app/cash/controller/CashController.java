package com.example.demo.app.cash.controller;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.order.entity.Order;
import com.example.demo.app.order.exception.OrderIdNotMatchedException;
import com.example.demo.app.order.exception.OrderNotEnoughRestCashException;
import com.example.demo.app.order.service.OrderService;
import com.example.demo.app.security.dto.MemberContext;
import com.example.demo.util.Ut;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cash")
public class CashController {
    private final OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final MemberService memberService;

    private final String SECRET_KEY = "test_sk_jkYG57Eba3GlO4NlxnkVpWDOxmA1";

    @RequestMapping("/charge")
    public String chargeCash(

            @RequestParam String paymentKey,
            @RequestParam Long amount,
            @RequestParam String orderId,
            Model model,
            @AuthenticationPrincipal MemberContext memberContext
    ) throws Exception {
        long orderIdInputed = Long.parseLong(orderId.split("__")[0]);
        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        Member member = memberContext.getMember();



        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            memberService.addCash(member, amount, "토스페이");

            return "redirect:/order/%d?msg=%s".formatted(orderIdInputed, Ut.url.encode("충전이 완료되었습니다."));
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "cash/charge/fail";
        }
    }
    @RequestMapping("/charge/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }
}
