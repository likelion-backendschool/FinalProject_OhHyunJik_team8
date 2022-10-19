package com.example.demo.app.member.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;

@Data
public class PostProfileReq {

    @NotEmpty
    private String email;
    @Nullable
    private String nickname;
}
