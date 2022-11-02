package com.example.demo.app.order.service;

import com.example.demo.app.cart.entity.CartItem;
import com.example.demo.app.cart.service.CartService;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.member.service.MemberService;
import com.example.demo.app.mybook.service.MyBookService;
import com.example.demo.app.order.entity.Order;
import com.example.demo.app.order.entity.OrderItem;
import com.example.demo.app.order.repository.OrderItemRepository;
import com.example.demo.app.order.repository.OrderRepository;
import com.example.demo.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MyBookService myBookService;

    @Transactional
    public Order createFromCart(Member buyer) {
        /**
         * 입력된 회원의 장바구니 아이템들을 전부 가져온다.
         * 만약에 특정 장바구니의 상품옵션이 판매불능이면 삭제
         * 만약에 특정 장바구니의 상품옵션이 판매가능이면 주문품목으로 옮긴 후 삭제
         * */
        List<CartItem> cartItems = cartService.getItemsByBuyer(buyer);
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }

            cartService.removeItem(cartItem);
        }

        return create(buyer, orderItems);
    }

    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {
        Order order = Order
                .builder()
                .buyer(buyer)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        // 주문 품목으로 부터 이름을 만든다.
        order.makeName();
        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void payByRestCashOnly(Order order) {
        Member buyer = order.getBuyer();
        long restCash = buyer.getRestCash();
        int payPrice = order.calculatePayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(buyer, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        order.setPaymentDone();
        orderRepository.save(order);
        // 결제된 정보를 토대로 주문 품복들을 마이북 서비스 단으로 넘겨서 저장을 한다.
        List<OrderItem> orderItems = order.getOrderItems();
        myBookService.createMyBookList(buyer,orderItems);
    }

    @Transactional
    public void refund(Order order) {
        int payPrice = order.getPayPrice();
        memberService.addCash(order.getBuyer(), payPrice, "주문__%d__환불__예치금".formatted(order.getId()));

        order.setRefundDone();
        orderRepository.save(order);
    }

    public Optional<Order> findForPrintById(long id) {return findById(id);}

    private Optional<Order> findById(long id) {return orderRepository.findById(id);}

    public boolean actorCanSee(Member actor, Order order) {return actor.getId().equals(order.getBuyer().getId());}

    @Transactional
    public void payByTossPayments(Order order, long useRestCash) {
        Member buyer = order.getBuyer();
        int payPrice = order.calculatePayPrice();
        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        if ( useRestCash > 0 ) {
            memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        }

        order.setPaymentDone();
        orderRepository.save(order);

        // 결제된 정보를 토대로 주문 품복들을 마이북 서비스 단으로 넘겨서 저장을 한다.
        List<OrderItem> orderItems = order.getOrderItems();
        myBookService.createMyBookList(buyer,orderItems);
    }

    public boolean actorCanPayment(Member actor, Order order) {return actorCanSee(actor, order);}

    public List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }

    public List<Order> getOrders(Member buyer) {
        List<Order> orderList = orderRepository.findAllByBuyerId(buyer.getId());
        return orderList;
    }

    @Transactional
    public void delete(Long id) {
        orderItemRepository.deleteAllByOrderId(id);
        orderRepository.deleteById(id);
    }
}
