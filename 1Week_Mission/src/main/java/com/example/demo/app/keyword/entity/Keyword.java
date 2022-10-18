package com.example.demo.app.keyword.entity;

import com.example.demo.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Keyword {
    @Id
    @EqualsAndHashCode.Include
    private String keyword;

    public String getListUrl() {
        return "/article/list?kwType=keyword&kw=%s".formatted(keyword);
    }
}