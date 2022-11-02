package com.example.demo.app.withdraw.dto;

import com.example.demo.app.member.entity.Member;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PostWithDrawReq {
    @NotEmpty
    private String accountName;
    @NotEmpty
    private String accountNumber;

    private long requestCash;
}
