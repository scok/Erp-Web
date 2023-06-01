package com.Erp.controller;

import com.Erp.dto.TransactionData;
import com.Erp.dto.TransactionDto;
import com.Erp.entity.Transaction;
import com.Erp.repository.TransactionRepository;
import com.Erp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService ;
    private final TransactionRepository transactionRepository ;



    @PostMapping(value = "/transaction/data")
    @ResponseBody
    public ResponseEntity<TransactionData> getTransactionData(@RequestParam MultiValueMap<String, String> formData) {
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));
        int num = Integer.parseInt(formData.get("searchType").get(0));
        String nameParam = formData.get("columns["+num+"][search][value]").get(0);

        String orderColumnIndexList = formData.get("columnIndex").get(0);
        String orderDirList = formData.get("orderDir").get(0);

        String orderColumnIndex = orderColumnIndexList != null && !orderColumnIndexList.isEmpty() ? orderColumnIndexList : null;
        String orderDir = orderDirList != null  ? orderDirList : null;

        int page = start / length;
        int size = length;

        Pageable pageable;

        if (orderColumnIndex != null && orderDir != null) {
            int columnIndex = Integer.parseInt(orderColumnIndex);
            Sort.Direction direction = orderDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            String columnName = transactionService.getColumnNameByIndex(columnIndex); // 컬럼 인덱스를 컬럼 이름으로 변환하여 사용

            pageable = PageRequest.of(page, size, direction, columnName);
        } else {
            pageable = PageRequest.of(page, size);
        }

        int total;
        List<Transaction> data;

        if (nameParam != null && !nameParam.isEmpty()) {

            if(num==0){
                total = (int) transactionRepository.countByName(nameParam);
                data = transactionRepository.findcompanyNum(nameParam, pageable);
            }else {
                total = (int) transactionRepository.countByName(nameParam);
                data = transactionRepository.findDataByName(nameParam, pageable);
            }

        } else {
            total = (int) transactionRepository.count();
            data = transactionRepository.findAllData(pageable);
        }



        TransactionData response = new TransactionData();
        response.setDraw(draw);
        response.setData(data);
        response.setRecordTotal(total);
        response.setRecordFiltered(total);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction/select")
    public @ResponseBody ResponseEntity<List<Transaction>> responseEntity(@RequestParam("companyName") String companyName){

        List<Object[]> transactions = transactionRepository.findDateAndAmount(companyName);

        List<Transaction> transactionList = new ArrayList<>();

        for (Object[] obj: transactions){

            Date trDate = (Date)obj[0];
            Long amount = (Long)obj[1];
            String companyNames = (String)obj[2];



            Transaction transaction = new Transaction();
            transaction.setTrDate(trDate);
            transaction.setAmount(amount);
            transaction.setCompanyName(companyNames);


            transactionList.add(transaction);
        }
        return ResponseEntity.ok(transactionList);
    }

    @GetMapping(value = "/transaction/select")
    @ResponseBody
    public  List<Transaction> selectName(){
        List<Object[]> data = transactionRepository.findSelectName();
        List<Transaction> transactions = new ArrayList<>();

        for (Object[] obj :data){
            Transaction transaction = new Transaction() ;
            String companyname = (String)obj[0];
            Date trDate = (Date)obj[1];
            Long amount = (Long)obj[2];

            transaction.setCompanyName(companyname);
            transaction.setTrDate(trDate);
            transaction.setAmount(amount);

            transactions.add(transaction);
        }

        return transactions ;

    }



//    @GetMapping(value = "/transaction/chart")
//    public @ResponseBody List<Transaction> getTransactionDataList(){
//        List<Object[]> data = transactionRepository.findTrDataList();
//        List<Transaction> transactions = new ArrayList<>();
//
//        for (Object[] obj : data) {
//            Date trDate = (Date)obj[0];
//            Long amount = (Long)obj[1];
//
//            Transaction transaction = new Transaction();
//            transaction.setTrDate(trDate);
//            transaction.setAmount(amount);
//
//            transactions.add(transaction);
//        }
//
//        return transactions;
//    }


    @GetMapping(value = "/transaction/form")
    public String datatableTran(){

        return "/financial/transactionForm" ;
    }




    @PostMapping(value = "/transaction/new")
    public String leadTransaction(@ModelAttribute("TransactionDto") @Valid TransactionDto dto, BindingResult err, Model model){
        if(err.hasErrors()){

            return "/financial/transactionEx";
        }
        try {
            transactionService.saveTransaction(dto);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage","이야야야야");
            return "/financial/transactionEx";
        }
        return "redirect:/";
    }


    @GetMapping(value = "/transaction/new")
    public String inTransaction(Model model){

        model.addAttribute("TransactionDto", new TransactionDto());
        return "/financial/transactionEx" ;
    }


}
