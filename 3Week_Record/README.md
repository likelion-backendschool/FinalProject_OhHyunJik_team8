<h1>3주차 내용</h1>
### 체크 리스트(22/10/31~22/11/02)

---

✔️ 관리자 회원(우선순위 1)

- [x]  관리자 회원 등록 (user1 유저에게 권한 부여)
- [x]  관리자 페이지 만들기

✔️ 정산(우선순위2) 

- [x]  정산데이터 생성(Batch 사용 X)
- [x]  정산데이터 생성(Batch 사용 O)
- [x]  건당 정산 처리
- [x]  일괄 정산 처리

✔️ 출금 기능(우선순위 3)

- [x]  예치금 출금신청 구현
- [x]  예치근 출금승인 구현

### 기능 설명

---

1. 관리자
    1. 1명의 유저에게만 관리자 권한을 부여한다. 
    2. 관리자는 관리자 페이지 접근과 관리자 기능 처리가 가능하다. 
2. 정산
    1. 정산 데이터 생성 : 월별 정산 데이터 생성
    2. Batch를 통한 정산 데이터 생성 : 배치 실행하는 날짜 기준 월별 정산 데이터 생성.
    3. 정산 데이터 조회 : 월별 정산 데이터를 조회
    4. 정산 승인 : 각 정산 데이터를 승인 함으로 해당 사용자의 예치금   
    5. 주문 리스트 : 주문 리스트들을 조회 가능, 각 상태에 따라 결제 완료, 미결제, 환불 완료로 조회가 가능
    6. 주문 상세 : 미 결제 상태 일 경우, 결제 할 수 있게 페이지 구현, 결제 완료된 주문일 경우, 상품에 대한 정보만 조회 
    7. 주문 취소 : 미 결제 된 상품일 경우 주문 취소 가능하게 구현 
3. 결제
    1. 예치금 충전 : 예치금이 없을 시, 예치금을 충전 할 수 있는 기능 구현
    2. 결제 :  PG 사 연동해서 결제 구현
    3. 환불 : 결제 완료하고 1분 이내 결제 취소 할 수 있게 구현 

### 개발일지

---

### 22/10/31

- 관리자
    - [x]  관리자 회원 생성(프론트 구현 O)
    - [x]  관리자 페이지 구현(프론트 구현 O)

### 22/11/01

- 정산
    - [x]  월별 정산 데이터 생성(프론트 구현 O, Batch 미 구현)
    - [x]  정산 처리 기능 구현(프론트 구현 O)

### 22/11/02

- 정산
    - [x]  스프링 배치를 통한 월별 정산 데이터 생성(프론트 구현 O)
- 출금
    - [x]  출금 신청 구현(프론트 구현 O)
    - [x]  출금 신청 승인(프론트 구현 O)

### 에러일지

---

- 이슈 6. Batch 실행 오류 - 2022/11/01
    - 문제 : 실행 시, `java.lang.IllegalStateException: Failed to execute CommandLineRunner` 에러 나오는 것으로 초기 데이터 생성 도중에 오류가 나오는 것 확인
    - 원인 :
    - 해결 : 초기 데이터 생성 부분에 initDataDone 참 여부로 수행
    
    ```java
    private boolean initDataDone = false; // 두번 실행 방지를 위해
    
        @Bean
        CommandLineRunner initData(
                MemberService memberService, PostService postService, ProductService productService, KeywordService keywordService, CartService cartService, CashService cashService, OrderService orderService) {
            return args -> {
                if (initDataDone) return;
    
                initDataDone = true;
                before(memberService,postService,productService,keywordService,cartService, cashService,orderService);
            };
        }
    ```
    

### 2주차 피드백 반영

---

- 개행 수정
    - 코드 라인 줄이려고 했던 방식이 가독성이 안 좋다는 피드백 받고 수정
- `MemberContext @Setter` 삭제
    - 처리 완료

### 참고 사이트

---

- 스프링 Batch
    - **[[스프링/Spring] Batch 구조와 구성 요소](https://deeplify.dev/back-end/spring/batch-architecture-and-components)**
    - ****[Spring batch+Scheduler 구현 해보기](https://dalgun.dev/blog/2019-10-30/spring-batch)****