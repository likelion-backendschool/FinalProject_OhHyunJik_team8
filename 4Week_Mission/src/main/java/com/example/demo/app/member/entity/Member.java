package com.example.demo.app.member.entity;

import com.example.demo.app.base.entity.BaseEntity;
import com.example.demo.app.mybook.dto.ProductRes;
import com.example.demo.util.Ut;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String nickname;
    private long restCash;
    @Column(columnDefinition = "TEXT")
    private String accessToken;

    public String getName() {
        return username;
    }

    public Member(long id) {
        super(id);
    }

    public void updateInfo(String email,String nickname){
        this.email = email;
        this.nickname = nickname;
    }

    public void updateRestCash(long restCash){this.restCash = restCash;}

    public static Member fromMap(Map<String, Object> map) {
        return fromJwtClaims(map);
    }

    public static Member fromJwtClaims(Map<String, Object> jwtClaims) {
        long id = 0;

        if (jwtClaims.get("id") instanceof Long) {
            id = (long) jwtClaims.get("id");
        } else if (jwtClaims.get("id") instanceof Integer) {
            id = (long) (int) jwtClaims.get("id");
        }

        LocalDateTime createDate = null;
        LocalDateTime modifyDate = null;

        if (jwtClaims.get("createDate") instanceof List) {
            createDate = Ut.date.bitsToLocalDateTime((List<Integer>) jwtClaims.get("createDate"));
        }

        if (jwtClaims.get("modifyDate") instanceof List) {
            modifyDate = Ut.date.bitsToLocalDateTime((List<Integer>) jwtClaims.get("modifyDate"));
        }

        String username = (String) jwtClaims.get("username");
        String email = (String) jwtClaims.get("email");
        String accessToken = (String) jwtClaims.get("accessToken");

        return Member
                .builder()
                .id(id)
                .createDate(createDate)
                .modifyDate(modifyDate)
                .username(username)
                .email(email)
                .accessToken(accessToken)
                .build();
    }

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        return authorities;
    }

    public Map<String, Object> getAccessTokenClaims() {
        return Ut.mapOf(
                "id", getId(),
                "createDate", getCreateDate(),
                "modifyDate", getModifyDate(),
                "username", getUsername(),
                "email", getEmail(),
                "authorities", getAuthorities()
        );
    }

    public Map<String, Object> toMap() {
        return Ut.mapOf(
                "id", getId(),
                "createDate", getCreateDate(),
                "modifyDate", getModifyDate(),
                "username", getUsername(),
                "email", getEmail(),
                "accessToken", getAccessToken(),
                "authorities", getAuthorities()
        );
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
