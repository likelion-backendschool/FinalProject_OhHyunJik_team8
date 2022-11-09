package com.example.demo.app.product.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class PostProductReq {
    @NotEmpty
    private String subject;
    private int price;
    @NotEmpty
    private String keyword;

}
