package com.Erp.repository;

import com.Erp.constant.TransactionCategory;
import com.Erp.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    @DisplayName("거래 저장 테스트")
    public void createTransaction(){

        for (int i = 0; i < 100; i++){
            Transaction transaction = new Transaction();

            transaction.setAmount((long) ((i+1)*100));
            transaction.setTransactionCategory(TransactionCategory.OUTS);
            transaction.setQuarter(1);
            transaction.setRemark("");

            Date now = new Date();
            transaction.setTrDate(now);
            transaction.setCompanyName("abcd" + i);

            transactionRepository.save(transaction);
        }
    }
}
