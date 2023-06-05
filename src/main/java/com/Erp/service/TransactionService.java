package com.Erp.service;

import com.Erp.dto.TransactionDto;
import com.Erp.entity.Transaction;
import com.Erp.entity.WarehousingInAndOut;
import com.Erp.repository.MemberRepository;
import com.Erp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository ;
    private final MemberRepository memberRepository ;


    public void inAndOut(WarehousingInAndOut warehousingInAndOut) {

        Transaction transaction = new Transaction();

        LocalDateTime inAndOutDateTime = warehousingInAndOut.getInAndOutDate();
        Instant instant = inAndOutDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date trDate = Date.from(instant);

        transaction.setTrDate(trDate);
        transaction.setCompanyName(warehousingInAndOut.getOrderSheetDetail().getOrderSheet().getAccount().getAcName());
        transaction.setAmount(warehousingInAndOut.getOrderSheetDetail().getOsSupplyValue()+warehousingInAndOut.getOrderSheetDetail().getOsTaxAmount());
        transaction.setRemark("fff");
        transaction.setQuarter(4);


        transactionRepository.save(transaction);

    }

    public Long saveTransaction(TransactionDto dto){

        Transaction transaction = dto.getTransaction();
        transactionRepository.save(transaction);

        return transaction.getId().longValue();
    }

    public String getColumnNameByIndex(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "id";
            case 1:
                return "companyName";
            case 2:
                return "amount";
            case 3:
                return "trDate";
            case 4:
                return "quarter";
            case 5:
                return "remark";
            case 6:
                return "transactionCategory";
            default:
                return "";
        }
    }

    public List<TransactionDto> getTransaction() {

        List<Transaction> transactionList = transactionRepository.findAll();
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactionList){
            TransactionDto dto = TransactionDto.of(transaction);
            transactionDtos.add(dto);
        }

        return transactionDtos ;
    }




    public List<TransactionDto> getTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();

        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactionList){
            TransactionDto dto = TransactionDto.of(transaction);
            transactionDtos.add(dto);
        }

        return transactionDtos ;
    }

    public List<TransactionDto> getIncomeTransactions(Integer year, Integer quarter) {
        List<Transaction> transactionList = transactionRepository.findTransactionList(year, quarter);

        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactionList){
            TransactionDto dto = TransactionDto.of(transaction);
            transactionDtos.add(dto);
        }

        return transactionDtos ;
    }



}
