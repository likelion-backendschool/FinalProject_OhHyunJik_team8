### 체크 리스트(22/11/07~22/11/09)

---

✔️ 유저(우선순위 1)

- [x]  기존 로그인 JWT 기반으로 변경
- [x]  로그인 API 개발
- [x]  로그인한 회원정보 구현
- [x]  엑세스 토큰 화이트리스트 구현

✔️ 구입 도서(우선순위2) 

- [x]  내가 구입한 도서리스트 구현
- [x]  내가 구입한 도서 상세정보 구현

✔️ 프론트 연동(우선순위 3)

- [x]  리액트 환경에서 작동 구현

✔️ Srping Doc(우선순위 4)

- [x]  Srping Doc 으로 API 문서화

### 기능 설명

---

1. 유저 
    1. 로그인 JWT 방식으로 변경, 로그인 시, AccessToken 발급
    2. Member 테이블에 accessToken 칼럼을 추가하여 화이트 리스트 구현
2. 도서
    1. 도서 리스트 : 유저가 구매한 도서 리스트를 조회하는 기능
    2. 도서 상세 페이지 : 특정 구매 도서에 대한 정보를 조회, 도서에 등록된 키워드 기준으로 bookChapter 를 조회 가능
3. 스프링 docs
    1. API 문서화 : http://localhost:8010/swagger-ui/index.html 를 통해서 api 문서 관리

### 개발일지

---

### 22/11/07

- 유저
    - [x]  로그인 방식 JWT 로 전환(진행)

### 22/11/08

- 유저
    - [x]  로그인 방식 JWT로 전환(구현 완료)

### 22/11/09

- 도서
    - [x]  구입 도서 리스트 조회(구현완료)
    - [x]  구입 도서 상세 페이지 조회(구현완료)
- 프론트
    - [x]  서버 프론트랑 연동(구현완료)
- 스프링 Docs
    - [x]  Swagger 를 통해 API 문서 관리

### 에러일지

---

- 이슈 6. 프론트 연동
    - 문제 : 프론트 환경에서 실행 시, cors 오류 발생
    - 원인 : Spring Security에 cors 접근에 대한 처리를 안한것으로 생긴 오류
    - 해결 : CorsConfigurationSource 관련 내용 security에 추가
    
    ```java
    @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
    
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod("*");
    
            UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);
            return urlBasedCorsConfigurationSource;
        }
    ```
