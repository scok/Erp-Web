package com.Erp.controller;

import com.Erp.dto.IncomeData;
import com.Erp.dto.IncomeDto;
import com.Erp.dto.SaveDto;
import com.Erp.repository.IncomeRepository;
import com.Erp.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeRepository incomeRepository;
    private final IncomeService incomeService;

    @GetMapping(value = "/incomes/income")
    public String goIncome(){ return "/financial/incomeForm"; }

    @GetMapping(value = "/incomes/dataSetting")
    @ResponseBody
    public IncomeData getList(@Valid IncomeData data, @RequestParam MultiValueMap<String, String> formData) {

        Short year = 0;

        if(formData.get("columns[28][search][value]") != null){

            Pattern pattern = Pattern.compile("\\d{4}");
            String searchValue = formData.get("columns[28][search][value]").toString();
            Matcher matcher = pattern.matcher(searchValue);

            if(matcher.find()){
                String value = matcher.group(0);
                year = Short.parseShort(value);
            }else{
                System.out.println("데이터 없음");
            }
        }

        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        int total = (int)incomeRepository.count();

        List<IncomeDto> incomeDtos = new ArrayList<>();

        if(year == 0){
            incomeDtos = incomeRepository.findIncomesList();
        }else{
            incomeDtos = incomeRepository.findSearchList(year);
        }

        data.setDraw(draw);
        data.setRecordsFiltered(total);
        data.setRecordsTotal(total);
        data.setData(incomeDtos);

        return data;
    }

    @PostMapping("/incomes/dataAdd")
    public @ResponseBody ResponseEntity addData(@RequestBody IncomeDto dto){

        System.out.println("addData called");

        Short year = incomeService.addData(dto);

        return new ResponseEntity(dto.getYear(), HttpStatus.OK);
    }

    @PostMapping("/incomes/dataSave")
    public @ResponseBody ResponseEntity saveData(@RequestBody SaveDto dto) throws Exception {

        System.out.println("saveData called");

        Long id = incomeService.updateData(dto);

        return new ResponseEntity(id, HttpStatus.OK);
    }

}
