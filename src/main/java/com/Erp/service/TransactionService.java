package com.Erp.service;

import com.Erp.constant.DivisionStatus;
import com.Erp.constant.TransactionCategory;
import com.Erp.dto.TransactionDto;
import com.Erp.entity.Income;
import com.Erp.entity.Transaction;
import com.Erp.entity.logistics.WarehousingInAndOut;
import com.Erp.repository.IncomeRepository;
import com.Erp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository ;
    private final IncomeRepository incomeRepository;
    private final IncomeService incomeService;


    public void inAndOut(WarehousingInAndOut warehousingInAndOut) {
        Transaction transaction = new Transaction();
        //LocalDateTime -> Date 형변환
        LocalDateTime inAndOutDateTime = warehousingInAndOut.getInAndOutDate();
        Instant instant = inAndOutDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date trDate = Date.from(instant);

        transaction.setTrDate(trDate);
        transaction.setCompanyName(warehousingInAndOut.getOrderSheetDetail().getOrderSheet().getAccount().getAcName());
        transaction.setAmount(warehousingInAndOut.getOrderSheetDetail().getOsSupplyValue()+warehousingInAndOut.getOrderSheetDetail().getOsTaxAmount());

        if (warehousingInAndOut.getDivisionStatus()==DivisionStatus.입고){
            transaction.setTransactionCategory(TransactionCategory.INS);
        }else{
            transaction.setTransactionCategory(TransactionCategory.OUTS);
        }

        int month =inAndOutDateTime.getMonthValue();
        int quarter ;

        if (1 <= month && 3 >= month){
            quarter = 1 ;
        }else if (4 <= month && 6 >= month){
            quarter = 2 ;
        }else if (7 <= month && 9 >= month){
            quarter = 3 ;
        }else {
            quarter = 4 ;
        }

        transaction.setQuarter(quarter);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getTrDate());
        Short year = (short) calendar.get(Calendar.YEAR);

        transaction.setWarehousingInAndOut(warehousingInAndOut);

        Income income = incomeRepository.findIncomeYearAndQuarter(year, transaction.getQuarter());

        if(income != null){
            System.out.println(income.getTransactions());

            income.getTransactions().add(transaction);

            incomeService.saveData2(income);

            transaction.setIncome(income);
        }

        transactionRepository.save(transaction);
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
