package com.example.demo.app.hashtag.entity;

import com.example.demo.app.base.entity.BaseEntity;
import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.post.entity.Post;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class HashTag extends BaseEntity {
    @ManyToOne
    @ToString.Exclude
    private Post post;
    @ManyToOne
    @ToString.Exclude
    private Keyword keyword;
}