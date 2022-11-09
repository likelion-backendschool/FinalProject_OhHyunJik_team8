package com.example.demo.app.security.dto;


import com.example.demo.app.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@JsonIncludeProperties({"id", "createDate", "modifyDate", "username", "email","nickname","emailVerified"})
public class MemberContext extends User {
    private final Long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String username;
    private final String email;
    private final String nickname;
    private final Set<GrantedAuthority> authorities;
    private boolean emailVerified;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);

        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.authorities = member.getAuthorities().stream().collect(Collectors.toSet());
        if(!member.getEmail().isEmpty()){
            this.emailVerified = true;
        }
    }

    public MemberContext(Member member) {
        super(member.getUsername(), member.getPassword(), member.getAuthorities());

        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.authorities = member.getAuthorities().stream().collect(Collectors.toSet());
        if(!member.getEmail().isEmpty()){
            this.emailVerified = true;
        }
    }

    public Member getMember() {
        return Member
                .builder()
                .id(id)
                .createDate(createDate)
                .modifyDate(modifyDate)
                .username(username)
                .email(email)
                .nickname(nickname)
                .build();
    }

    public String getName() {
        return getUsername();
    }

    public boolean memberIs(Member member) {
        return id.equals(member.getId());
    }

    public boolean memberIsNot(Member member) {
        return memberIs(member) == false;
    }
}