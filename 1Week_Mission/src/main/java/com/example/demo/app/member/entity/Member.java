package com.example.demo.app.member.entity;

import com.example.demo.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
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

    public Member(long id) {
        super(id);
    }
}
