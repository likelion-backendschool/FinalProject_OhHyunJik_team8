package com.example.demo.app.post.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PostWriteReq {

    @NotEmpty
    private String subject;
    @NotEmpty
    private String content;


}
