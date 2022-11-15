# FinalProject_OhHyunJik_team8
> 매주 차 운영진의 요구사항서를 토대로 4주동안 진행한 스프링 기반 프로젝트(2022.10.17~2022.11.11)

### 기술 스택

- Front : HTML, JS
- Server : Spring Boot, Spring Security, Spring Batch,Spring Docs, Junit, JWT
- DB : JPA, Maria DB
- ETC : Swagger, Toss Payment

## 상세 서비스 설명
    1. 서비스는 eBook 마켓과 eBook Reader로 이루어짐
    2. 모든 회원은 작가라는 책 판매자로 등록이 가능하다.
    3. 작가는 해시태그 기반 키워드를 글에 등록 할 수 있다.
    4. 작가은 여러 개의 글로 구성된 eBook 상품을 가격을 붙여 등록이 가능하다.
    5. 회원은 등록된 상품을 앱 서비스 내 포인트 결제 또는 Toss Payment 로 결제가 가능하다.
    6. 결제한 상품은 MyBook으로 등록 되어서 열람이 가능하다.
    7. 서비스에서는 결제 완료된 상품 매월 15일 새벽 4시에 정산 시스템을 통해서 작가에게 포인트로 지급이 된다.
