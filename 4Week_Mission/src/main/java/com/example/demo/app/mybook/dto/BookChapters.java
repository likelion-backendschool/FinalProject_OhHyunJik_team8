package com.example.demo.app.mybook.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class BookChapters {
    private Long id;
    private String subject;
    private String content;
    private String contentHtml; // 미구현
}
