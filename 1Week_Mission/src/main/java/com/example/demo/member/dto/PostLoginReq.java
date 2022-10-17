package com.example.demo.member.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
@Data
public class PostLoginReq {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    @Nullable
    private String nickname;
}
