package com.Erp.service;

import com.Erp.dto.TransactionDto;
import com.Erp.entity.Income;
import com.Erp.entity.Transaction;
import com.Erp.repository.IncomeRepository;
import com.Erp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository ;
    private final IncomeRepository incomeRepository;
    private final IncomeService incomeService;


    public Long saveTransaction(TransactionDto dto){

        Transaction transaction = dto.getTransaction();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dto.getTrDate());
        int year = calendar.get(Calendar.YEAR);

        Income income = incomeRepository.findIncomeYearAndQuarter((short)year ,dto.getQuarter());

        if(income != null){
            transaction.setIncome(income);
            transactionRepository.save(transaction);

            List<Transaction> transactions = income.getTransactions();

            if(transactions.size() == 0){
                transactions = new ArrayList<>();
            }
            transactions.add(transaction);

            income.setTransactions(transactions);

            incomeService.updateTotalData(income, income.getTransactions());

            incomeRepository.save(income);
        }else {
            transactionRepository.save(transaction);
        }

        return transaction.getId();
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
