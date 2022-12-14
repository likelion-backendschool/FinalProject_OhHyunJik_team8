<h1>2주차 내용</h1>
### 체크 리스트(22/10/24~22/10/26)

---

✔️ 장바구니(우선순위 1)

- [x]  장바구니 등록 구현
- [x]  장바구니 리스트
- [x]  장바구니 품목삭제

✔️ 주문(우선순위 2)

- [x]  주문생성
- [x]  주문리스트
- [x]  주문상세
- [x]  주문취소

✔️ 결제(우선순위 3)

- [x]  결제 구현
- [x]  환불 처리(테스트 위해 1분 이내 환불)

### 기능 설명

---

1. 장바구니
    1. 장바구니 등록 : 상품 상세페이지에 장바구니 추가 버튼 으로 상품을 유저 장바구니에 추가
    2. 장바구니 리스트 : 등록한 상품들을 조회
    3. 장바구니 삭제 : 리스트에 들어있는 상품을 목록에 삭제
2. 주문
    1. 주문 생성 : 장바구니 에 품목들을 주문 테이블에 데이터 생성, 이때 미결제 상태
    2. 주문 리스트 : 주문 리스트들을 조회 가능, 각 상태에 따라 결제 완료, 미결제, 환불 완료로 조회가 가능
    3. 주문 상세 : 미 결제 상태 일 경우, 결제 할 수 있게 페이지 구현, 결제 완료된 주문일 경우, 상품에 대한 정보만 조회
    4. 주문 취소 : 미 결제 된 상품일 경우 주문 취소 가능하게 구현
3. 결제
    1. 예치금 충전 : 예치금이 없을 시, 예치금을 충전 할 수 있는 기능 구현
    2. 결제 :  PG 사 연동해서 결제 구현
    3. 환불 : 결제 완료하고 1분 이내 결제 취소 할 수 있게 구현

### 개발일지

---

### 22/10/24

- 장바구니
    - [x]  장바구니 등록 구현(프론트 구현 O)
    - [x]  장바구니 삭제 구현(프론트 구현 O)
    - [x]  장바구니 주문 페이지로 넘기기(프론트 구현 O)

### 22/10/25

- 주문
    - [x]  주문 상세 페이지 구현(프론트 구현 O)
    - [x]  예치금 충전 기능 구현(프론트 구현 O)
- 결제
    - [x]  결제 기능 구현(프론트 구현 O)

### 22/10/26

- 주문
    - [x]  주문 리스트 구현(프론트 구현 O)
    - [x]  미 결제된 주문 일 경우, 주문 삭제 기능 구현(프론트 구현 O)
- 결제
    - [x]  결제 완료후, 환불하기 구현(프론트 구현 O)

### 에러일지

---

- 이슈 4. 예치금 동기화 오류 - 2022/10/24
    - 문제 : 예치금 충전 시 기존 예치금 + 충전 예치금이 되는것이 아닌, 충천한 예치금으로 수정 되는것으로 확인
    - 원인 : `MemberContext` 에만 수정이 되고 DB 에 적용이 안되서 생기는 오류 확인
    - 해결 : DB 테이블에 수정 설정 후 정상적으로 예치금 충전 되는 것 확인
- 이슈 5. 주문 삭제 - 2022/10/25
    - 문제 : 미 결제된 주문 삭제 구현 도중, 특이한 이슈 없이 삭제가 안되는 것으로 확인
    - 원인 : `OrderService` 에 `@Transactional(readOnly = true)` 를 적용하고, 삭제 메서드에 `@Transactional` 을 적용 안시켜서 생긴 이슈
    - 해결  : 해당 메서드에 `@Transactional` 어노테이션 추가 후, 정상적으로 동작

### 1주차 피드백 반영

---

- 코드 부분부분 이상한 개행
    - 최대한 일관성 있게 작성 하였지만, 아직 부족한 게 많았음
- 키워드 별 Post 검색 기능 미포함
    - 구현 완료
- Post 삭제, 수정 후 리다이렉션 오류
    - 수정 후, 정상 동작하는것 확인
- 회원가입시 아이디, email 중복 확인 미포함
    - 프론트, 서버에 해당 기능 구현 완료
    - 중복 여부에 따라 회원가입 버튼 비활성화 기능은 미 포함
- Entity에서 Setter 사용하지 말기
    - 모든 도메인에 `@Setter` 삭제
