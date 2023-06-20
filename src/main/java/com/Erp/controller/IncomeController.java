package com.Erp.controller;

import com.Erp.dto.IncomeData;
import com.Erp.dto.IncomeDto;
import com.Erp.dto.SaveDto;
import com.Erp.dto.UserDto;
import com.Erp.repository.IncomeRepository;
import com.Erp.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    // 손익계산서 페이지 이동
    @GetMapping(value = "/incomes/income")
    public String goIncome(){ return "/financial/incomeForm"; }

    // 손익계산서 페이지 로딩 및 갱신
    @GetMapping(value = "/incomes/dataSetting")
    @ResponseBody
    public IncomeData getList(@Valid IncomeData data, @RequestParam MultiValueMap<String, String> formData) {

        // 갱신할 연도를 찾음
        Pattern pattern = Pattern.compile("\\d{4}");
        String searchValue = formData.get("columns[33][search][value]").toString();
        Matcher matcher = pattern.matcher(searchValue);

        Short year = 0;

        if(matcher.find()){
            String value = matcher.group(0);
            year = Short.parseShort(value);
        }else{
            System.out.println("데이터 없음");
        }

        int draw = Integer.parseInt(formData.get("draw").get(0));

        int total = (int)incomeRepository.count();

        List<IncomeDto> incomeDtos = new ArrayList<>();

        // 연도에 따라 손익계산서 데이터 찾기
        if(year == 0){ // 값이 없을 때
            incomeDtos = incomeService.findIncomesList();
        }else{
            incomeDtos = incomeService.findIncomesList(year);
        }

        data.setDraw(draw);
        data.setRecordsFiltered(total);
        data.setRecordsTotal(total);
        data.setData(incomeDtos);

        return data;
    }

    // 손익계산서 추가
    @PostMapping("/incomes/dataAdd")
    public @ResponseBody ResponseEntity addData(@RequestBody IncomeDto dto, HttpServletRequest request){

        System.out.println("addData called");

        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute("User");

        Short year = 0;

        if(user.getDepartment().equals("재무")){ // 재무 팀만 등록 가능하도록 제한
            year = incomeService.addData(dto);
        }

        return new ResponseEntity(year, HttpStatus.OK);
    }

    // 손익계산서 수정
    @PostMapping("/incomes/dataSave")
    public @ResponseBody ResponseEntity saveData(@RequestBody SaveDto dto) throws Exception {

        System.out.println("saveData called");

        Long id = incomeService.updateData(dto);

        return new ResponseEntity(id, HttpStatus.OK);
    }

}
