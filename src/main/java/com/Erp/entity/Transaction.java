package com.Erp.entity;

import com.Erp.constant.TransactionCategory;
import com.Erp.dto.TransactionDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.Date;

@Setter @Getter @Entity
    @Table(name = "Transactions")
    public class Transaction {
        @Id
        @Column(name = "transaction_num",unique = true)
        @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ; // 거래번호

    @Column( nullable = false)
    private String companyName ; // 거래(공급)처 명
    @Column( nullable = false)
    private Long amount ; // 거래금액
    @Column( nullable = false)
    private Date trDate ; // 거래일자
    @Column( nullable = false)
    private Integer quarter ; // 분기
    @Column( nullable = false)
    private String remark ; // 주석
    @Enumerated(EnumType.STRING)
    private TransactionCategory transactionCategory; // 거래분류


//    @Builder.Default
//    private int draw = 1;//이 개체가 응답하는 그리기 카운터-데이터 요청의 일부로 전송 된 그리기 매개 변수
//    @Builder.Default
//    private int start = 0; //페이징 첫 번째 레코드 표시기. 이것은 현재 데이터 세트의 시작점
//    @Builder.Default
//    private int length = 10; //테이블이 현재 그리기에서 표시 할 수있는 레코드 수
//    @Builder.Default
//    private int page = 0; //페이징수 - 실제 jpa 검색시에는 페이징 번호를 통해 검색
//
//    public int getPage(){
//        page = (start/length);
//        return page;
//    }



    private static ModelMapper modelMapper =  new ModelMapper();

    public static Transaction of(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto,Transaction.class);
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "income_num")
    private Income income ;



}

