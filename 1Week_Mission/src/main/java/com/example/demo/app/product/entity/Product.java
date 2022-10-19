package com.example.demo.app.product.entity;

import com.example.demo.app.base.entity.BaseEntity;
import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Product extends BaseEntity {
    @ManyToOne
    private Member seller;
    @ManyToOne
    private Keyword postKeyword;
    private String subject;
    private int price;
}
