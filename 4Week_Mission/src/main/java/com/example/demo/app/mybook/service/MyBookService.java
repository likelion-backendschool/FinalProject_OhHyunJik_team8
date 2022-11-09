package com.example.demo.app.mybook.service;

import com.example.demo.app.member.entity.Member;
import com.example.demo.app.mybook.dto.BookChapters;
import com.example.demo.app.mybook.dto.MyBookListRes;
import com.example.demo.app.mybook.dto.MyBookDetail;
import com.example.demo.app.mybook.entity.MyBook;
import com.example.demo.app.mybook.repository.MyBookRepository;
import com.example.demo.app.order.entity.OrderItem;
import com.example.demo.app.post.entity.Post;
import com.example.demo.app.post.service.PostService;
import com.example.demo.app.product.entity.Product;
import com.example.demo.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;
    private final PostService postService;
    @Transactional
    public void createMyBook(Member member, Product product){
        MyBook myBook = MyBook.builder()
                .book(product)
                .member(member)
                .build();
        myBookRepository.save(myBook);
    }

    @Transactional
    public void createMyBookList(Member member, List<OrderItem> orderItems){
        for(OrderItem orderItem: orderItems){
            createMyBook(member,orderItem.getProduct());
        }
    }

    public List<MyBookListRes> findAll(MemberContext memberContext) {
        List<MyBook> myBookList = myBookRepository.findAllByMemberId(memberContext.getId());
        List<MyBookListRes> result = new ArrayList<>();
        for(MyBook mb : myBookList){
            MyBookListRes myBookListRes = MyBookListRes.builder()
                    .id(mb.getId())
                    .createDate(mb.getCreateDate())
                    .modifyDate(mb.getModifyDate())
                    .ownerId(mb.getMember().getId())
                    .product(mb.getBook().productRes())
                    .build();
            result.add(myBookListRes);
        }

        return result;
    }

    public MyBookDetail findById(MemberContext memberContext, Long myBookId) {
        MyBook myBook = myBookRepository.findByIdAndMemberId(myBookId,memberContext.getId());
        List<BookChapters> bookChaptersList = postService.BookChapter(myBook.getBook().getPostKeyword().getContent()) ;
        MyBookDetail productDetail = MyBookDetail.builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getModifyDate())
                .product(myBook.getBook().productDetailRes(bookChaptersList))
                .build();
        return productDetail;
    }
}
