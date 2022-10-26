package com.example.demo.app.post.entity;


import com.example.demo.app.base.entity.BaseEntity;
import com.example.demo.app.hashtag.entity.HashTag;
import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.member.entity.Member;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Post extends BaseEntity {
    @ManyToOne
    private Member author;
    private String subject;
    private String content;

    public String getExtra_inputValue_hashTagContents() {
        Map<String, Object> extra = getExtra();
        if (extra.containsKey("hashTags") == false) {
            return "";
        }
        List<HashTag> hashTags = (List<HashTag>) extra.get("hashTags");
        if (hashTags.isEmpty()) {
            return "";
        }
        return hashTags
                .stream()
                .map(hashTag -> "#" + hashTag.getKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public String getExtra_hashTagLinks() {
        Map<String, Object> extra = getExtra();
        if (extra.containsKey("hashTags") == false) {
            return "";
        }
        List<HashTag> hashTags = (List<HashTag>) extra.get("hashTags");
        if (hashTags.isEmpty()) {
            return "";
        }
        return hashTags
                .stream()
                .map(hashTag -> {
                    String text = "#" + hashTag.getKeyword().getContent();

                    return """
                            <a href="%s" target="_blank">%s</a>
                            """
                            .stripIndent()
                            .formatted(hashTag.getKeyword().getListUrl(), text);
                })
                .sorted()
                .collect(Collectors.joining(" "));
    }
    public void updatePost(String subject, String content){
        this.subject = subject;
        this.content = content;
    }
}