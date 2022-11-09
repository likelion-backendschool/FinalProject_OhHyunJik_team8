package com.example.demo.app.product.entity;

import com.example.demo.app.base.entity.BaseEntity;
import com.example.demo.app.keyword.entity.Keyword;
import com.example.demo.app.member.entity.Member;
import com.example.demo.app.mybook.dto.BookChapters;
import com.example.demo.app.mybook.dto.MyBookDetailProduct;
import com.example.demo.app.mybook.dto.ProductRes;
import com.example.demo.app.post.entity.Post;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Product extends BaseEntity {
    @ManyToOne
    private Member seller;
    @ManyToOne
    private Keyword postKeyword;
    private String subject;
    private int price;

    public Product(long id) {
        super(id);
    }

    public int getSalePrice() {
        return getPrice();
    }

    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.5);
    }

    public boolean isOrderable() {
        return true;
    }

    public String getJdenticon() {
        return "product__" + getId();
    }

    public ProductRes productRes(){
        ProductRes productRes = ProductRes.builder()
                .id(getId())
                .createDate(getCreateDate())
                .modifyDate(getModifyDate())
                .authorId(seller.getId())
                .authorName(seller.getName())
                .subject(getSubject())
                .build();
        return productRes;
    }

    public MyBookDetailProduct productDetailRes(List<BookChapters> bookChaptersList){
        MyBookDetailProduct myBookDetailProduct = MyBookDetailProduct.builder()
                .id(getId())
                .authorId(seller.getId())
                .authorName(seller.getName())
                .subject(getSubject())
                .bookChapters(bookChaptersList)
                .build();
        return myBookDetailProduct;
    }
}
