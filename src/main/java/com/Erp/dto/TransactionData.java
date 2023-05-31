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
    private int draw;
    private int recordTotal;
    private int recordFiltered;
    private List<Transaction> data;

    public List<Transaction> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }
}
