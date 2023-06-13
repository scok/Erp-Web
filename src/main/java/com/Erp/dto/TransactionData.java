package com.Erp.dto;

import com.Erp.entity.Transaction;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
public class TransactionData {
    private int start ;
    private int length ;
    private int draw;
    private int recordTotal;
    private int recordFiltered;
    private List<Transaction> data;
    private int totalPages; // 총 페이지 수
    private int currentPage;

    public List<Transaction> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

}
