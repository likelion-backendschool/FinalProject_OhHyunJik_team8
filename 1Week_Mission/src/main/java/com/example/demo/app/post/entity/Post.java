package com.example.demo.app.post.entity;


import com.example.demo.app.base.entity.BaseEntity;
import com.example.demo.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Post extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member author;
    private String subject;
    private String content; // 마크다운 문법
}
