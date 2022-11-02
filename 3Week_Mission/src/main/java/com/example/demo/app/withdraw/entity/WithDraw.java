package com.example.demo.app.withdraw.entity;

import com.example.demo.app.base.entity.BaseEntity;
import com.example.demo.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class WithDraw extends BaseEntity {
    @ManyToOne
    private Member member;

    private String accountName;
    private String accountNumber;
    private long requestCash;

    private String status;
}
