package com.example.demo.app.mybook.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class MyBookDetail {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long ownerId;
    private MyBookDetailProduct product;
}
