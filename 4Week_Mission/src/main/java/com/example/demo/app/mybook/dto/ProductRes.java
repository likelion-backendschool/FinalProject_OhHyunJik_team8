package com.example.demo.app.mybook.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
public class ProductRes {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long authorId;
    private String authorName;
    private String subject;
    private List<BookChapters> bookChapters;
}
