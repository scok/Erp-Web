package com.Erp.dto;

import com.Erp.constant.TransactionCategory;
import com.Erp.entity.Transaction;
import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter@Getter@ToString
public class TransactionDto {

    private Long id ; // 거래번호
    @NotEmpty(message = "꼭")
    private String companyName ; // 거래(공급)처 명
    @NotNull(message = "꼭")
    private Long amount ; // 거래금액
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trDate ; // 거래일자
    @NotNull(message = "꼭")
    private Integer quarter ; // 분기
    private TransactionCategory transactionCategory; // 거래분류

    private static ModelMapper modelMapper = new ModelMapper() ;



    public Transaction getTransaction(){
        System.out.println("post3");
        return modelMapper.map(this,Transaction.class) ;
    }

    public static TransactionDto of(Transaction transaction){
        return modelMapper.map(transaction,TransactionDto.class);
    }


//    private List<WarehouseStocks> warehouseStocksList ; // 재고리스트
//    private List<WarehouseOuts> warehouseOutsList ; // 출고리스트
//    private List<WarehouseIns> warehouseInsList ; // 입고리스트


}
