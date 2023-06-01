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

    private static ModelMapper modelMapper =  new ModelMapper();

    public static Transaction of(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto,Transaction.class);
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_id")
    private Income income ;
}

